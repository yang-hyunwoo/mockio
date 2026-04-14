package com.mockio.core_service.interview.service;

/**
 * 인터뷰 답변 저장 메인 흐름 (Orchestration)
 *
 * 전체 흐름:
 * 1. 인터뷰/질문 검증
 * 2. idempotency 검증 및 중복 요청 처리
 * 3. 답변 저장
 * 4. 빈 답변이면 AI 처리 없이 다음 질문
 * 5. 정상 답변이면 이벤트 발행
 * 6. follow-up 질문 생성 시도
 * 7. deep-dive 질문 생성 시도
 * 8. 둘 다 없으면 다음 질문 or 인터뷰 종료
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_ai_contractor.constant.InterviewEndReason;
import com.mockio.common_ai_contractor.constant.InterviewMode;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveCommand;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveDecision;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveValid;
import com.mockio.common_ai_contractor.generator.deepdive.GeneratedDeepDiveBundle;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.followup.FollowupValid;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.interview.Mapper.InterviewQuestionMapper;
import com.mockio.core_service.interview.constant.QuestionType;
import com.mockio.core_service.interview.domain.Interview;
import com.mockio.core_service.interview.domain.InterviewAnswer;
import com.mockio.core_service.interview.domain.InterviewQuestion;
import com.mockio.core_service.interview.dto.request.InterviewAnswerRequest;
import com.mockio.core_service.interview.dto.response.InterviewQuestionAnswerDetailResponse;
import com.mockio.core_service.interview.dto.response.InterviewQuestionReadResponse;
import com.mockio.core_service.interview.dto.response.SttResponse;
import com.mockio.core_service.interview.forward.ai.AIServiceClient;
import com.mockio.core_service.interview.kafka.domain.OutboxInterviewEvent;
import com.mockio.core_service.interview.kafka.dto.request.InterviewAnswerSkippedPayload;
import com.mockio.core_service.interview.kafka.dto.request.InterviewAnswerSubmittedPayload;
import com.mockio.core_service.interview.kafka.dto.request.InterviewCompletedPayload;
import com.mockio.core_service.interview.kafka.dto.response.InternalInterviewAnswerDetailResponse;
import com.mockio.core_service.interview.kafka.repository.OutboxInterviewEventRepository;
import com.mockio.core_service.interview.repository.InterviewAnswerRepository;
import com.mockio.core_service.interview.repository.InterviewQuestionRepository;
import com.mockio.core_service.interview.repository.InterviewRepository;
import com.mockio.core_service.interview.repository.querydsl.InterviewAnswerQueryDslRepository;
import com.mockio.core_service.interview.util.DeepDiveGate;
import com.mockio.core_service.interview.util.followup.FollowUpDecider;
import com.mockio.core_service.interview.util.followup.FollowUpDecision;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterviewAnswerService {

    private static final String EVENT_TOPIC_FEEDBACK = "FEEDBACK";
    private static final String EVENT_ANSWER_SUBMITTED = "InterviewAnswerSubmitted";
    private static final String EVENT_NO_ANSWER_SKIPPED = "InterviewNoAnswerSkipped";
    private static final String EVENT_INTERVIEW_COMPLETED = "InterviewCompleted";

    private static final int FOLLOW_UP_LIMIT_PERCENT = 60;
    private static final int FOLLOW_UP_SEQ_GAP = 5;
    private static final int DEEP_DIVE_SEQ_GAP = 1;

    private final InterviewAnswerRepository interviewAnswerRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewAnswerQueryDslRepository interviewAnswerQueryDslRepository;
    private final InterviewRepository interviewRepository;
    private final FollowUpDecider followUpDecider;
    private final AIServiceClient aiServiceClient;
    private final OutboxInterviewEventRepository outboxInterviewEventRepository;
    private final ObjectMapper objectMapper;
    private final DeepDiveGate deepDiveGate;

    @Transactional
    public InterviewQuestionReadResponse interviewAnswerSave(Long userId, InterviewAnswerRequest request) {

        // 1. 인터뷰 접근 권한 검증 (userId 기반)
        Interview interview = findInterview(userId, request.interviewId());

        // 2. 해당 인터뷰에 속한 질문인지 검증
        InterviewQuestion question = findQuestion(request);

        // 3. idempotency key 필수 검증 (중복 요청 방지)
        validateIdempotencyKey(request);

        // 4. 동일 요청이면 DB 조회 없이 기존 결과 재구성
        Optional<InterviewQuestionReadResponse> duplicatedResponse = findDuplicatedResponse(interview, question, request);
        if (duplicatedResponse.isPresent()) {
            return duplicatedResponse.get();
        }

        // 5. 답변 저장 (attempt 증가 포함)
        SavedAnswerContext savedContext = saveAnswer(interview, question, request);

        // 6. 답변이 비어있으면 AI 처리 없이 skip 이벤트만 발행
        if (isBlankAnswer(request.answerText())) {
            publishNoAnswerSkippedEvent(interview, question, savedContext.answer());
            return moveNextOrComplete(interview, question);
        }

        // 7.답변은 있지만 회피성 답변일 경우는 AI 호출
        if(isNoAnswer(request.answerText())) {
            publishAnswerSubmittedEvent(interview, question, savedContext);
            return moveNextOrComplete(interview, question);
        }

        // 8. follow-up 판단 (룰 기반)
        FollowUpDecision decision = followUpDecider.decide(question, request, interview);

        // 9. 답변 제출 이벤트 발행 (AI 피드백용)
        publishAnswerSubmittedEvent(interview, question, savedContext);

        // 10. follow-up 가능 여부 계산 (비율 제한 + 타입 체크)
        FollowUpAvailability availability = calculateFollowUpAvailability(interview, question, decision);

        // 11. 일반 follow-up 질문 생성 시도
        Optional<InterviewQuestionReadResponse> followUpResponse =
                tryCreateFollowUp(interview, question, request, savedContext, availability);

        if (followUpResponse.isPresent()) {
            return followUpResponse.get();
        }

        // 12. deep-dive 질문 생성 시도
        Optional<InterviewQuestionReadResponse> deepDiveResponse =
                tryCreateDeepDive(interview, question, request, savedContext, availability);

        if (deepDiveResponse.isPresent()) {
            return deepDiveResponse.get();
        }

        // 13. 둘 다 없으면 다음 질문 또는 인터뷰 종료
        return moveNextOrComplete(interview, question);
    }

    @Transactional(readOnly = true)
    public InterviewQuestionAnswerDetailResponse interviewAnswerRead(Long userId, Long questionId) {
        return interviewAnswerQueryDslRepository.interviewAnswerDetail(userId, questionId)
                .orElse(null);
    }



    @Transactional
    public SttResponse aiStt(MultipartFile file, Long interviewId, Long userId) {
        Interview interview = findInterview(userId, interviewId);

        if (interview.getInterviewMode() != InterviewMode.VOICE) {
            throw new CustomApiException(
                    INTERVIEW_FORBIDDEN.getHttpStatus(),
                    INTERVIEW_FORBIDDEN,
                    INTERVIEW_FORBIDDEN.getMessage()
            );
        }

        return aiServiceClient.generateStt(file);
    }

    @Transactional(readOnly = true)
    public List<InternalInterviewAnswerDetailResponse> getInterviewList(Long interviewId) {
        return interviewAnswerRepository.findDetailsByInterviewId(interviewId);
    }

    private Interview findInterview(Long userId, Long interviewId) {
        return interviewRepository.findByIdAndUserId(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_FORBIDDEN.getHttpStatus(),
                        INTERVIEW_FORBIDDEN,
                        INTERVIEW_FORBIDDEN.getMessage()
                ));
    }

    private InterviewQuestion findQuestion(InterviewAnswerRequest request) {
        return interviewQuestionRepository
                .findByIdAndInterviewId(request.questionId(), request.interviewId())
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));
    }

    private void validateIdempotencyKey(InterviewAnswerRequest request) {
        if (request.idempotencyKey() == null || request.idempotencyKey().isBlank()) {
            throw new CustomApiException(
                    IDEMPOTENCY_KEY_NOT_FOUND.getHttpStatus(),
                    IDEMPOTENCY_KEY_NOT_FOUND,
                    IDEMPOTENCY_KEY_NOT_FOUND.getMessage()
            );
        }
    }

    private Optional<InterviewQuestionReadResponse> findDuplicatedResponse(
            Interview interview,
            InterviewQuestion question,
            InterviewAnswerRequest request
    ) {
        return interviewAnswerRepository
                .findByQuestionIdAndIdempotencyKey(question.getId(), request.idempotencyKey())
                .map(answer -> rebuildResponse(interview, question));
    }

    /**
     * 답변 저장 로직
     *
     * - attempt 증가
     * - 기존 current answer 제거
     * - 새로운 answer 저장
     *
     *  DataIntegrityViolationException:
     *  → 동시에 동일 요청이 들어온 경우 (idempotency race condition)
     *  → 이미 저장된 것으로 판단하고 복구 흐름으로 처리
     */
    private SavedAnswerContext saveAnswer(
            Interview interview,
            InterviewQuestion question,
            InterviewAnswerRequest request
    ) {
        int nextAttempt = interviewAnswerRepository
                .findMaxAttemptByQuestionId(request.questionId())
                .orElse(0) + 1;

        InterviewAnswer answer = InterviewAnswer.createAnswer(
                question,
                nextAttempt,
                request.answerText(),
                request.answerDurationSeconds()
        );
        answer.updateAnswer(request.idempotencyKey());

        try {
            interviewAnswerRepository.unsetCurrentByQuestionId(request.questionId());
            interviewAnswerRepository.save(answer);
            interview.incrementAnswered();
            return new SavedAnswerContext(answer, nextAttempt);
        } catch (DataIntegrityViolationException e) {
            interviewAnswerRepository
                    .findByQuestionIdAndIdempotencyKey(question.getId(), request.idempotencyKey())
                    .orElseThrow(() -> e);

            return new SavedAnswerContext(answer, nextAttempt, true);
        }
    }

    private void publishNoAnswerSkippedEvent(
            Interview interview,
            InterviewQuestion question,
            InterviewAnswer answer
    ) {
        log.info(
                "Skip AI feedback/follow-up/deep-dive because answer is blank. interviewId={}, questionId={}, answerId={}",
                interview.getId(),
                question.getId(),
                answer.getId()
        );

        InterviewAnswerSkippedPayload payload = new InterviewAnswerSkippedPayload(
                interview.getId(),
                question.getId(),
                answer.getId()
        );

        publishEvent(EVENT_TOPIC_FEEDBACK, answer.getId(), EVENT_NO_ANSWER_SKIPPED, payload);
    }

    private void publishAnswerSubmittedEvent(
            Interview interview,
            InterviewQuestion question,
            SavedAnswerContext savedContext
    ) {
        InterviewAnswerSubmittedPayload payload = new InterviewAnswerSubmittedPayload(
                interview.getId(),
                question.getId(),
                question.getQuestionText(),
                savedContext.answer().getId(),
                savedContext.answer().getAnswerText(),
                savedContext.nextAttempt(),
                interview.getTrack().getLabel(),
                interview.getDifficulty().name(),
                interview.getFeedbackStyle().name(),
                question.getPrimaryTag()
        );

        publishEvent(EVENT_TOPIC_FEEDBACK, savedContext.answer().getId(), EVENT_ANSWER_SUBMITTED, payload);
    }

    private FollowUpAvailability calculateFollowUpAvailability(
            Interview interview,
            InterviewQuestion question,
            FollowUpDecision decision
    ) {
        int baseQuestionCount = interviewQuestionRepository.countByInterviewIdAndType(
                interview.getId(),
                QuestionType.BASE
        );

        int followUpCount = interviewQuestionRepository.countByInterviewIdAndType(
                interview.getId(),
                QuestionType.FOLLOW_UP
        );

        boolean canAsk = canAskFollowUp(baseQuestionCount, followUpCount);
        boolean isBaseQuestion = question.getType() == QuestionType.BASE;

        return new FollowUpAvailability(canAsk, isBaseQuestion);
    }

    /**
     * 일반 follow-up 질문 생성
     *
     * 조건:
     * - follow-up 비율 제한 통과 (60% 룰)
     * - base 질문일 것
     * - FollowUpDecider가 요청한 경우
     *
     * 흐름:
     * 1. AI에게 follow-up 질문 생성 요청
     * 2. seq = 기존 질문 + 5 (일반 follow-up 간격)
     * 3. DB 저장
     * 4. answer에 follow-up 이유 기록
     */
    private Optional<InterviewQuestionReadResponse> tryCreateFollowUp(
            Interview interview,
            InterviewQuestion question,
            InterviewAnswerRequest request,
            SavedAnswerContext savedContext,
            FollowUpAvailability availability
    ) {
        if (!availability.canAsk() || !availability.isBaseQuestion()) {
            return Optional.empty();
        }

        FollowUpDecision ruleDecision = followUpDecider.decide(question, request, interview);

        //1차 에서 걸리면 꼬리 질문 x
        if (ruleDecision.shouldSkip()) {
            return Optional.empty();
        }

        //꼬리질문 ai 생성 확인 후 꼬리 질문 생성
        if(ruleDecision.shouldDeferToAi()) {

            FollowUpQuestionCommand validateCommand = new FollowUpQuestionCommand(
                    interview.getTrack(),
                    interview.getDifficulty(),
                    interview.getFeedbackStyle(),
                    ruleDecision.reason(),   // 1차 힌트
                    new FollowUpQuestionCommand.QAPair(
                            question.getQuestionText(),
                            request.answerText()
                    )
            );

            // AI로 꼬리질문 필요 여부 + 사유 확인
            FollowupValid followupValid = aiServiceClient.generateFollowQuestionsValid(validateCommand);

            if (!followupValid.shouldFollowUp()) {
                return Optional.empty();
            }

            // 실제 질문 생성은 AI가 판단한 사유/포커스를 사용
            InterviewQuestionReadResponse response = getInterviewQuestionReadResponse(interview, question, savedContext, request,followupValid.reason() );

            return Optional.of(response);
        }
        return Optional.empty();
    }

    private InterviewQuestionReadResponse getInterviewQuestionReadResponse(Interview interview,
                                                                           InterviewQuestion question,
                                                                           SavedAnswerContext savedContext,
                                                                           InterviewAnswerRequest request,
                                                                           String reason) {
        FollowUpQuestionCommand generateCommand = new FollowUpQuestionCommand(
                interview.getTrack(),
                interview.getDifficulty(),
                interview.getFeedbackStyle(),
                reason,
                new FollowUpQuestionCommand.QAPair(
                        question.getQuestionText(),
                        request.answerText()
                )
        );


        FollowUpQuestion generated = aiServiceClient.generateFollowQuestions(generateCommand);
        FollowUpQuestion.Item item = generated.questions();

        int nextSeq = question.getSeq() + FOLLOW_UP_SEQ_GAP;
        int nextDepth = question.getDepth() + 1;

        InterviewQuestion followUpQuestion = InterviewQuestion.createFollowUp(
                interview,
                nextSeq,
                question.getId(),
                nextDepth,
                question.getId(),
                null,
                item.title(),
                item.primaryTag(),
                item.tags(),
                item.body(),
                item.provider(),
                item.model(),
                item.promptVersion(),
                item.temperature()
        );

        return saveGeneratedQuestion(
                interview,
                followUpQuestion,
                nextSeq,
                savedContext.answer(),
                reason
        );
    }

    /**
     * deep-dive 질문 생성
     *
     * 조건:
     * - deepDiveGate 통과 (AI 호출 여부 판단)
     *    - 난이도 HARD/VERY_HARD
     *    - 꼬리질문일 경우만
     *    - 너무 짧지 않은 답변
     *    - SHALLOW_PATTERNS 패턴 사용
     * - 이미 deep-dive 생성되지 않았을 것
     *
     * 특징:
     * - seq +1 (바로 다음 질문으로 깊이 파고듦)
     * - context 문자열로 추적 정보 저장
     *
     *  deep-dive는 "추가 꼬리질문"이 아니라
     *     "심층 질문 흐름"으로 별도 분기임
     */
    private Optional<InterviewQuestionReadResponse> tryCreateDeepDive(
            Interview interview,
            InterviewQuestion question,
            InterviewAnswerRequest request,
            SavedAnswerContext savedContext,
            FollowUpAvailability availability
    ) {
        // 1. follow-up 이후 질문에서만 deep-dive 허용
        if (question.getType() != QuestionType.FOLLOW_UP) {
            return Optional.empty();
        }

        // 2. 비율 제한만 체크
        if (!availability.canAsk()) {
            return Optional.empty();
        }

        boolean deepDiveCandidate = deepDiveGate.shouldCallAiForDeepDive(
                interview,
                request,
                question.getType()
        );

        if (!deepDiveCandidate) {
            return Optional.empty();
        }

        boolean alreadyDeepDived = interviewQuestionRepository
                .existsByInterviewIdAndParentQuestionIdAndType(
                        interview.getId(),
                        question.getId(),
                        QuestionType.DEEP_DIVE
                );

        if (alreadyDeepDived) {
            return Optional.empty();
        }

        //deep-dive 인 경우는 기본 질문 + 꼬리 질문을 전송
        String basicQuestionText = interviewQuestionRepository.findById(question.getParentQuestionId())
                .map(InterviewQuestion::getQuestionText)
                .orElseThrow(() -> new CustomApiException(
                        QUESTION_NOT_FOUND.getHttpStatus(),
                        QUESTION_NOT_FOUND,
                        QUESTION_NOT_FOUND.getMessage()
                ));

        String basicAnswerText = interviewAnswerRepository.findByQuestionId(question.getParentQuestionId())
                .map(InterviewAnswer::getAnswerText)
                .orElseThrow(() -> new CustomApiException(
                        QUESTION_NOT_FOUND.getHttpStatus(),
                        QUESTION_NOT_FOUND,
                        QUESTION_NOT_FOUND.getMessage()
                ));
        DeepDiveCommand command = new DeepDiveCommand(
                interview.getTrack(),
                interview.getDifficulty(),
                basicQuestionText,
                basicAnswerText,
                question.getQuestionText(),
                request.answerText()
        );

        //ai 호출로 딥다이브 질문 생성 가능 여부 체크
        DeepDiveValid deepDiveValid = aiServiceClient.generateDeepDiveValid(command);

        if(!deepDiveValid.shouldDeepDive()) {
            return Optional.empty();
        }

        GeneratedDeepDiveBundle result = aiServiceClient.generateDeepDiveResult(command);
        DeepDiveDecision decision = result == null ? null : result.decision();

        if (decision == null || !decision.shouldFollowUp()) {
            return Optional.empty();
        }

        FollowUpQuestion deepDiveQuestion = result.question();
        if (deepDiveQuestion == null || deepDiveQuestion.questions() == null) {
            return Optional.empty();
        }

        FollowUpQuestion.Item item = deepDiveQuestion.questions();

        int nextSeq = question.getSeq() + DEEP_DIVE_SEQ_GAP;
        int nextDepth = question.getDepth() + 1;
        String deepDiveContext = buildDeepDiveContext(decision);

        InterviewQuestion generatedQuestion = InterviewQuestion.createDeepDive(
                interview,
                nextSeq,
                question.getId(),
                nextDepth,
                question.getId(),
                null,
                item.title(),
                item.primaryTag(),
                item.tags(),
                item.body(),
                item.provider(),
                item.model(),
                item.promptVersion(),
                item.temperature()
        );

        InterviewQuestionReadResponse response = saveGeneratedQuestion(
                interview,
                generatedQuestion,
                nextSeq,
                savedContext.answer(),
                deepDiveContext
        );

        return Optional.of(response);
    }

    private InterviewQuestionReadResponse saveGeneratedQuestion(
            Interview interview,
            InterviewQuestion generatedQuestion,
            int seq,
            InterviewAnswer answer,
            String followupReason
    ) {
        try {
            interviewQuestionRepository.save(generatedQuestion);
            interviewRepository.incrementTotalCount(interview.getId());
            answer.followupUpdate(followupReason);
            return InterviewQuestionMapper.fromList(List.of(generatedQuestion), false, interview);
        } catch (DataIntegrityViolationException e) {
            InterviewQuestion existingQuestion = interviewQuestionRepository
                    .findByInterviewIdAndSeq(interview.getId(), seq)
                    .orElseThrow(() -> e);

            return InterviewQuestionMapper.fromList(List.of(existingQuestion), false, interview);
        }
    }

    /**
     * outbox 테이블 저장
     */
    private void publishEvent(String topic, Long aggregateId, String eventType, Object payload) {
        JsonNode payloadJson = objectMapper.valueToTree(payload);

        outboxInterviewEventRepository.save(
                OutboxInterviewEvent.createNew(
                        topic,
                        aggregateId,
                        eventType,
                        payloadJson
                )
        );
    }

    /**
     * follow-up 생성 제한 정책
     *
     * followUpCount / baseQuestionCount < 60%
     *
     * 이유:
     * - follow-up이 과도하게 많아지는 것 방지
     */
    private boolean canAskFollowUp(int baseQuestionCount, int followUpCount) {
        return followUpCount * 100 < baseQuestionCount * FOLLOW_UP_LIMIT_PERCENT;
    }

    private boolean isBlankAnswer(String answerText) {
        return answerText == null || answerText.trim().isEmpty();
    }

    private InterviewQuestionReadResponse rebuildResponse(
            Interview interview,
            InterviewQuestion currentQuestion
    ) {
        return interviewQuestionRepository
                .findFirstByInterviewIdAndSeqGreaterThanOrderBySeqAsc(
                        interview.getId(),
                        currentQuestion.getSeq()
                )
                .map(nextQuestion -> InterviewQuestionMapper.fromList(List.of(nextQuestion), false, interview))
                .orElseGet(() -> InterviewQuestionMapper.fromList(List.of(), false, interview));
    }

    /**
     * 다음 질문 이동 또는 인터뷰 종료
     *
     * - 다음 seq 질문이 있으면 반환
     * - 없으면 인터뷰 종료 처리 + 이벤트 발행
     */
    private InterviewQuestionReadResponse moveNextOrComplete(
            Interview interview,
            InterviewQuestion currentQuestion
    ) {
        return interviewQuestionRepository
                .findFirstByInterviewIdAndSeqGreaterThanOrderBySeqAsc(
                        interview.getId(),
                        currentQuestion.getSeq()
                )
                .map(nextQuestion -> InterviewQuestionMapper.fromList(List.of(nextQuestion), false, interview))
                .orElseGet(() -> completeInterview(interview));
    }

    /**
     * 인터뷰 종료 처리
     *
     * - 상태 COMPLETED로 변경
     * - 완료 이벤트(outbox) 발행
     * - 클라이언트에는 "끝남" 응답 반환
     */
    private InterviewQuestionReadResponse completeInterview(Interview interview) {
        interview.complete(InterviewEndReason.COMPLETED);

        InterviewCompletedPayload payload = new InterviewCompletedPayload(
                interview.getUserId(),
                interview.getId(),
                interview.getTrack().name(),
                interview.getDifficulty().name(),
                interview.getFeedbackStyle().name()
        );

        publishEvent(EVENT_TOPIC_FEEDBACK, interview.getId(), EVENT_INTERVIEW_COMPLETED, payload);
        return InterviewQuestionMapper.fromList(List.of(), true, interview);
    }

    /**
     * deep-dive 메타 정보 문자열 생성
     *
     * 포함 정보:
     * - depth (1~3 제한)
     * - focus (핵심 주제)
     * - gaps (부족한 부분)
     * - reason (AI 판단 근거)
     *
     */
    private String buildDeepDiveContext(DeepDiveDecision decision) {
        String focus = (decision.focus() == null || decision.focus().isEmpty())
                ? ""
                : String.join(",", decision.focus());

        String gaps = (decision.gaps() == null || decision.gaps().isEmpty())
                ? ""
                : String.join(" / ", decision.gaps());

        int depth = Math.max(1, Math.min(3, decision.depth()));

        return "DEEPDIVE"
                + " depth=" + depth
                + " focus=" + focus
                + " gaps=" + gaps
                + " reason=" + (decision.reason() == null ? "" : decision.reason());
    }

    /**
     * 회피성 답변은 follow/deep-dive 무시
     * @param text
     * @return
     */
    private boolean isNoAnswer(String text) {
        String normalized = text.trim();
        return normalized.contains("모르겠")
                || normalized.contains("잘 모르")
                || normalized.contains("기억이 안");
    }

    private record SavedAnswerContext(
            InterviewAnswer answer,
            int nextAttempt,
            boolean duplicatedAfterInsert
    ) {
        private SavedAnswerContext(InterviewAnswer answer, int nextAttempt) {
            this(answer, nextAttempt, false);
        }
    }

    private record FollowUpAvailability(
            boolean canAsk,
            boolean isBaseQuestion
    ) {
    }

}
