package com.mockio.interview_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.interview_service.Mapper.InterviewQuestionMapper;
import com.mockio.interview_service.constant.QuestionType;
import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.domain.InterviewAnswer;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.dto.request.InterviewAnswerRequest;
import com.mockio.interview_service.dto.response.InterviewQuestionReadResponse;
import com.mockio.interview_service.forward.ai.AIServiceClient;
import com.mockio.interview_service.kafka.domain.OutboxInterviewEvent;
import com.mockio.interview_service.kafka.dto.request.InterviewAnswerSubmittedPayload;
import com.mockio.interview_service.kafka.dto.request.InterviewCompletedPayload;
import com.mockio.interview_service.kafka.repository.OutboxInterviewEventRepository;
import com.mockio.interview_service.repository.InterviewAnswerRepository;
import com.mockio.interview_service.repository.InterviewQuestionRepository;
import com.mockio.interview_service.repository.InterviewRepository;
import com.mockio.interview_service.util.followup.FollowUpDecider;
import com.mockio.interview_service.util.followup.FollowUpDecision;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;

@Service
@RequiredArgsConstructor
public class InterviewAnswerService {

    private final InterviewAnswerRepository interviewAnswerRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewRepository interviewRepository;
    private final FollowUpDecider followUpDecider;
    private final AIServiceClient aiServiceClient;
    private final OutboxInterviewEventRepository outboxInterviewEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public InterviewQuestionReadResponse interviewAnswerSave(String userId, InterviewAnswerRequest interviewAnswerRequest) {

        //인터뷰 질문 권한 체크
        Interview interview = findInterview(userId, interviewAnswerRequest.interviewId());

        //인터뷰 질문 존재 여부 확인
        InterviewQuestion interviewQuestion = interviewQuestionRepository
                .findByIdAndInterviewId(interviewAnswerRequest.questionId(), interviewAnswerRequest.interviewId())
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));

        if (interviewAnswerRequest.idempotencyKey() == null || interviewAnswerRequest.idempotencyKey().isBlank()) {
            throw new CustomApiException(IDEMPOTENCY_KEY_NOT_FOUND.getHttpStatus(),
                    IDEMPOTENCY_KEY_NOT_FOUND,
                    IDEMPOTENCY_KEY_NOT_FOUND.getMessage());
        }

        // 1) 같은 요청이면 기존 row 반환 (멱등)
        var existing = interviewAnswerRepository
                .findByQuestionIdAndIdempotencyKey(interviewQuestion.getId(), interviewAnswerRequest.idempotencyKey());

        if (existing.isPresent()) {
            return rebuildResponse(interview, interviewQuestion, existing.get(), interviewAnswerRequest);
        }
        // 2) attempt 계산
        int nextAttempt = interviewAnswerRepository.findMaxAttemptByQuestionId(interviewAnswerRequest.questionId()).orElse(0) + 1;

        //질문을 저장한다.
        InterviewAnswer answer = InterviewAnswer.createAnswer(interviewQuestion,
                nextAttempt,
                interviewAnswerRequest.answerText(),
                interviewAnswerRequest.answerDurationSeconds()
        );
        answer.updateAnswer(interviewAnswerRequest.idempotencyKey());

        try {
            interviewAnswerRepository.unsetCurrentByQuestionId(interviewAnswerRequest.questionId());

            interviewAnswerRepository.save(answer);

        } catch (DataIntegrityViolationException e) {
            // 5) 동시성/재시도 등으로 누군가 먼저 저장했을 수 있음 -> 다시 조회해서 반환
            var saved = interviewAnswerRepository
                    .findByQuestionIdAndIdempotencyKey(interviewQuestion.getId(), interviewAnswerRequest.idempotencyKey())
                    .orElseThrow(() -> e);

            return rebuildResponse(interview, interviewQuestion, saved, interviewAnswerRequest);
        }


        FollowUpDecision decision = followUpDecider.decide(interviewQuestion, interviewAnswerRequest, interview);

        int questionCount = interviewQuestionRepository.countByInterviewIdAndType(interviewAnswerRequest.interviewId(),QuestionType.BASE);
        int followUpCount = interviewQuestionRepository.countByInterviewIdAndType(interviewAnswerRequest.interviewId(), QuestionType.FOLLOW_UP);
        InterviewAnswerSubmittedPayload payload = new InterviewAnswerSubmittedPayload(
                interview.getId(),
                interviewQuestion.getId(),
                answer.getId(),
                nextAttempt,
                interview.getTrack().name(),
                interview.getDifficulty().name(),
                interview.getFeedbackStyle().name()
        );
        //outbox 피드백 저장
        JsonNode payloadJsonNode = objectMapper.valueToTree(payload);

        outboxInterviewEventRepository.save(
                OutboxInterviewEvent.createNew(
                        "FEEDBACK",
                        answer.getId(),
                        "InterviewAnswerSubmitted",
                        payloadJsonNode
                )
        );

        /**
         * 꼬리 질문 유효성 검사에 걸렸다면
         * ai서비스에서 꼬리 질문을 추가 후 꼬리 질문 리턴
         * 그렇지 않다면 다음 seq 리턴
         */
        if (decision.askFollowUp() && canAskFollowUp(questionCount,followUpCount)) {
            FollowUpQuestionCommand followUpQuestionCommand = new FollowUpQuestionCommand(
                    interview.getTrack(),
                    interview.getDifficulty(),
                    interview.getFeedbackStyle(),
                    decision.reason(),
                    new FollowUpQuestionCommand.QAPair(
                            interviewQuestion.getQuestionText(),
                            interviewAnswerRequest.answerText()
                    )
            );

            FollowUpQuestion followUpQuestion = aiServiceClient.generateFollowQuestions(followUpQuestionCommand);
            FollowUpQuestion.Item q = followUpQuestion.questions();

            InterviewQuestion saveFollowQuestion = InterviewQuestion.createFollowUp(
                    interview,
                    (interviewQuestion.getSeq() + 5),
                    interviewQuestion.getId(),
                    interviewQuestion.getDepth(),
                    interviewQuestion.getId(),
                    null,
                    q.questionText(),
                    q.provider(),
                    q.model(),
                    q.promptVersion(),
                    q.temperature()
            );

            interviewQuestionRepository.save(saveFollowQuestion);
            answer.followupUpdate(decision.reason());
            return InterviewQuestionMapper.fromList(List.of(saveFollowQuestion));

        } else {
            return interviewQuestionRepository
                    .findFirstByInterviewIdAndSeqGreaterThanOrderBySeqAsc(interviewAnswerRequest.interviewId(), interviewQuestion.getSeq())
                    .map(q -> InterviewQuestionMapper.fromList(List.of(q)))
                    .orElseGet(() -> {
                        interview.complete();

                        InterviewCompletedPayload completedPayload = new InterviewCompletedPayload(
                                interview.getId(),
                                interview.getTrack().name(),
                                interview.getDifficulty().name(),
                                interview.getFeedbackStyle().name()
                        );

                        //outbox 피드백 저장
                        JsonNode completedPayloadJsonNode = objectMapper.valueToTree(completedPayload);

                        outboxInterviewEventRepository.save(
                                OutboxInterviewEvent.createNew(
                                        "FEEDBACK",
                                        interview.getId(),
                                        "InterviewCompleted",
                                        completedPayloadJsonNode
                                )
                        );

                        return InterviewQuestionMapper.fromList(List.of());
                    });
        }

    }

    private Interview findInterview(String userId, Long interviewId) {
        return interviewRepository.findByIdAndUserId(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_FORBIDDEN.getHttpStatus(),
                        INTERVIEW_FORBIDDEN,
                        INTERVIEW_FORBIDDEN.getMessage()
                ));
    }

    private boolean canAskFollowUp(int totalCount, int followUpCount) {
        return followUpCount * 100 < totalCount * 60;
    }

    private InterviewQuestionReadResponse rebuildResponse(
            Interview interview,
            InterviewQuestion interviewQuestion,
            InterviewAnswer existingAnswer,
            InterviewAnswerRequest req
    ) {
        // 1) 다음 질문이 있는지 조회
        return interviewQuestionRepository
                .findFirstByInterviewIdAndSeqGreaterThanOrderBySeqAsc(
                        interview.getId(),
                        interviewQuestion.getSeq()
                )
                .map(q -> InterviewQuestionMapper.fromList(List.of(q)))
                .orElseGet(() -> {
                    // 이미 완료된 인터뷰라면 그냥 빈 응답
                    return InterviewQuestionMapper.fromList(List.of());
                });
    }
}
