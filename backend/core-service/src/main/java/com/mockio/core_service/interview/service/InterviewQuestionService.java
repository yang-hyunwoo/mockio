package com.mockio.core_service.interview.service;

/**
 * 인터뷰 질문 저장
 *
 * 전체 흐름:
 * 1. 유저 인터뷰 세팅 / 유저 인터뷰 생성 및 조회
 * 2. 질문 없을 경우 AI 호출
 * 3. 질문 목록 조회 후 질문 응답
 *
 * retry :
 * 1.유저 인터뷰 권한 조회
 * 2. 인터뷰 질문 조회 후 BASE 만 질문 저장
 */

import com.mockio.common_ai_contractor.constant.InterviewEndReason;
import com.mockio.common_ai_contractor.generator.question.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.question.GeneratedQuestion;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.interview.Mapper.InterviewQuestionMapper;
import com.mockio.core_service.interview.constant.QuestionType;
import com.mockio.core_service.interview.domain.Interview;
import com.mockio.core_service.interview.domain.InterviewQuestion;
import com.mockio.core_service.interview.domain.UserInterviewSetting;
import com.mockio.core_service.interview.dto.request.RetryInterviewRequest;
import com.mockio.core_service.interview.dto.request.StartInterviewRequest;
import com.mockio.core_service.interview.dto.response.InterviewQuestionReadResponse;
import com.mockio.core_service.interview.forward.ai.AIServiceClient;
import com.mockio.core_service.interview.repository.InterviewQuestionRepository;
import com.mockio.core_service.interview.repository.InterviewRepository;
import com.mockio.core_service.interview.repository.UserInterviewSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;
import static com.mockio.common_ai_contractor.constant.InterviewStatus.*;
import static java.time.OffsetDateTime.now;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewQuestionService {

    private final InterviewRepository interviewRepository;
    private final UserInterviewSettingRepository userInterviewSettingRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final AIServiceClient aiServiceClient;
    private final UserInterviewSettingService userInterviewSettingService;

    public InterviewQuestionReadResponse startInterview(Long userId, StartInterviewRequest request) {
        return generateAndSaveQuestions(generateInterview(userId,request), userId);
    }

    public InterviewQuestionReadResponse retryInterview(Long userId, RetryInterviewRequest request) {
        validateNoActiveInterview(userId);

        Interview sourceInterview = getRetrySourceInterview(userId, request.interviewId());
        Interview savedRetryInterview = createRetryInterview(userId, request.idempotencyKey(), sourceInterview);
        Interview lockedRetryInterview = getRetryInterviewForUpdate(userId, savedRetryInterview.getId());

        validateRetryInterviewStatus(lockedRetryInterview);

        if (lockedRetryInterview.isQuestionGenerated()) {
            return getQuestions(lockedRetryInterview.getId(), userId);
        }

        lockedRetryInterview.markGenerating();

        List<InterviewQuestion> baseQuestions = getBaseQuestions(sourceInterview.getId());
        List<InterviewQuestion> copiedQuestions = copyBaseQuestions(lockedRetryInterview, baseQuestions);

        interviewQuestionRepository.saveAll(copiedQuestions);
        lockedRetryInterview.markGenerated();

        return getQuestions(lockedRetryInterview.getId(), userId);
    }


    public Long generateInterview(Long userId, StartInterviewRequest request) {

        //1.유저 인터뷰 세팅 ( 없으면 생성 후 리턴)
        UserInterviewSetting userInterviewSetting = userInterviewSettingRepository.findByUserId(userId)
                .orElseGet(
                        () -> userInterviewSettingService.absentEnsureSettingSave(userId)
                );

        //2.유저 인터뷰 생성 및 조회 한다.
        Long activeInterviewId = resolveActiveInterview(userId);
        if (activeInterviewId != null) return activeInterviewId;

        return interviewRepository
                .findByUserIdAndIdempotencyKey(userId, request.idempotencyKey())
                .map(Interview::getId)
                .orElseGet(() -> {
                    try {
                        Interview interview = Interview.create(
                                request.idempotencyKey(),
                                userId,
                                userInterviewSetting.getTrack(),
                                userInterviewSetting.getDifficulty(),
                                userInterviewSetting.getFeedbackStyle(),
                                userInterviewSetting.getInterviewMode(),
                                userInterviewSetting.getAnswerTimeSeconds(),
                                userInterviewSetting.getInterviewQuestionCount()
                        );

                        return interviewRepository.save(interview).getId();

                    } catch (DataIntegrityViolationException e) {
                        return interviewRepository
                                .findByUserIdAndIdempotencyKey(userId, request.idempotencyKey())
                                .orElseThrow(() -> e)
                                .getId();
                    }
                });
    }

    public InterviewQuestionReadResponse generateAndSaveQuestions(Long interviewId, Long userId) {
        Interview interview = interviewRepository.findByIdAndUserIdForUpdate(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));

        if (interview.getStatus() != ACTIVE) {
            throw new CustomApiException(INVALID_INTERVIEW_STATUS.getHttpStatus(), INVALID_INTERVIEW_STATUS, INVALID_INTERVIEW_STATUS.getMessage());
        }

        // 중복 생성 방지: 정책 1) 이미 생성되었으면 기존 결과 반환
        if (interview.isQuestionGenerated()) {
            return getQuestions(interviewId, userId);
        }

        interview.markGenerating();

        try {
            List<Interview> byUserId = interviewRepository.findTop30ByUserIdOrderByCreatedAtDesc(userId);
            List<Long> idList = byUserId.stream()
                    .map(Interview::getId)
                    .toList();

            List<InterviewQuestion> tagList = interviewQuestionRepository.findTop30ByInterviewIdInAndPrimaryTagIsNotNullOrderByCreatedAtDesc(idList);
            List<String> primaryTagList = tagList.stream()
                    .map(InterviewQuestion::getPrimaryTag)
                    .distinct()
                    .toList();

            GenerateQuestionCommand cmd = new GenerateQuestionCommand(
                    userId,
                    interview.getTrack(),
                    interview.getDifficulty(),
                    interview.getInterviewMode(),
                    interview.getAnswerTimeSeconds(),
                    interview.getCount(),
                    primaryTagList
            );
            GeneratedQuestion generatedQuestion = aiServiceClient.generateQuestions(cmd);
            questionSave(interview, generatedQuestion);
            interview.markGenerated();

            return getQuestions(interviewId, userId);
        } catch (Exception e) {
            interview.markGenerateFailed(e.getMessage());
            throw new CustomApiException(AI_SERVICE_FAILED.getHttpStatus(), AI_SERVICE_FAILED, AI_SERVICE_FAILED.getMessage());
        }
    }

    protected void questionSave(Interview interview, GeneratedQuestion generatedQuestion) {
        List<InterviewQuestion> entites = new ArrayList<>();
        OffsetDateTime now = now();
        List<GeneratedQuestion.Item> questions = generatedQuestion.questions();
        for (GeneratedQuestion.Item q : questions) {
            entites.add(InterviewQuestion.createInterviewQuestion(
                    interview,
                    q.seq(),
                    q.title(),
                    q.primaryTag(),
                    q.tags(),
                    q.body(),
                    q.provider(),
                    q.model(),
                    q.promptVersion(),
                    q.temperature(),
                    now
            ));
        }
        interviewQuestionRepository.saveAll(entites);
    }

    @Transactional(readOnly = true)
    public InterviewQuestionReadResponse getQuestions(Long interviewId, Long userId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));
        if (interview.getUserId().equals(userId)) {
            return InterviewQuestionMapper.fromList(interviewQuestionRepository.findAllByInterviewIdOrderBySeqAsc(interviewId), false, interview);
        } else {
            throw new CustomApiException(INTERVIEW_FORBIDDEN.getHttpStatus(), INTERVIEW_FORBIDDEN, INTERVIEW_FORBIDDEN.getMessage());
        }
    }

    private void validateNoActiveInterview(Long userId) {
        if (interviewRepository.existsByUserIdAndStatus(userId, ACTIVE)) {
            throw new CustomApiException(
                    INTERVIEW_ALREADY_ACTIVE.getHttpStatus(),
                    INTERVIEW_ALREADY_ACTIVE,
                    INTERVIEW_ALREADY_ACTIVE.getMessage()
            );
        }
    }

    private Interview getRetrySourceInterview(Long userId, Long interviewId) {
        return interviewRepository.findByIdAndUserId(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_FORBIDDEN.getHttpStatus(),
                        INTERVIEW_FORBIDDEN,
                        INTERVIEW_FORBIDDEN.getMessage()
                ));
    }

    private Interview createRetryInterview(Long userId, String idempotencyKey, Interview sourceInterview) {
        Interview retryInterview = Interview.reInterviewCreate(
                idempotencyKey,
                userId,
                sourceInterview
        );

        return interviewRepository.save(retryInterview);
    }

    private Interview getRetryInterviewForUpdate(Long userId, Long interviewId) {
        return interviewRepository.findByIdAndUserIdForUpdate(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));
    }

    private void validateRetryInterviewStatus(Interview interview) {
        if (interview.getStatus() != ACTIVE) {
            throw new CustomApiException(
                    INVALID_INTERVIEW_STATUS.getHttpStatus(),
                    INVALID_INTERVIEW_STATUS,
                    INVALID_INTERVIEW_STATUS.getMessage()
            );
        }
    }

    private List<InterviewQuestion> getBaseQuestions(Long interviewId) {
        return interviewQuestionRepository.findByInterviewIdAndTypeOrderBySeqAsc(interviewId, QuestionType.BASE);
    }

    private List<InterviewQuestion> copyBaseQuestions(
            Interview retryInterview,
            List<InterviewQuestion> sourceQuestions
    ) {
        return sourceQuestions.stream()
                .map(source -> InterviewQuestion.createInterviewQuestion(
                        retryInterview,
                        source.getSeq(),
                        source.getTitle(),
                        source.getPrimaryTag(),
                        source.getTags() == null
                                ? new LinkedHashSet<>()
                                : new LinkedHashSet<>(source.getTags()),
                        source.getQuestionText(),
                        source.getProvider(),
                        source.getModel(),
                        source.getPromptVersion(),
                        source.getTemperature(),
                        now()
                ))
                .toList();
    }

    private Long resolveActiveInterview(Long userId) {
        return interviewRepository.findActiveByUserIdAndStatus(userId, ACTIVE)
                .map(interview -> {
                    boolean hasQuestions =
                            interviewQuestionRepository.countByInterviewId(interview.getId()) > 0;

                    if (hasQuestions) {
                        return interview.getId();
                    }

                    interview.complete(InterviewEndReason.ERROR);
                    return null;
                })
                .orElse(null);
    }
}
