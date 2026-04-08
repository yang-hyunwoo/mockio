package com.mockio.core_service.interview.service;

import com.mockio.common_ai_contractor.generator.question.GeneratedQuestion;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.interview.Mapper.InterviewQuestionMapper;
import com.mockio.core_service.interview.constant.QuestionType;
import com.mockio.core_service.interview.domain.Interview;
import com.mockio.core_service.interview.domain.InterviewQuestion;
import com.mockio.core_service.interview.domain.UserInterviewSetting;
import com.mockio.core_service.interview.dto.request.InterviewGenerateContext;
import com.mockio.core_service.interview.dto.request.RetryInterviewRequest;
import com.mockio.core_service.interview.dto.response.InterviewQuestionReadResponse;
import com.mockio.core_service.interview.repository.InterviewQuestionRepository;
import com.mockio.core_service.interview.repository.InterviewRepository;
import com.mockio.core_service.user.domain.UserProfile;
import com.mockio.core_service.user.dto.response.UserProfileDetailResponse;
import com.mockio.core_service.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.INTERVIEW_ALREADY_ACTIVE;
import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.INTERVIEW_FORBIDDEN;
import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.INTERVIEW_NOT_FOUND;
import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.INVALID_INTERVIEW_STATUS;
import static com.mockio.common_ai_contractor.constant.InterviewStatus.ACTIVE;
import static com.mockio.common_ai_contractor.constant.InterviewStatus.GENERATING;
import static com.mockio.common_ai_contractor.constant.InterviewStatus.PENDING;

@Service
@RequiredArgsConstructor
public class InterviewQuestionTxService {

    private final InterviewRepository interviewRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final UserInterviewSettingService userInterviewSettingService;

    @Transactional
    public InterviewGenerateContext prepareGenerate(Long interviewId, Long userId) {
        Interview interview = interviewRepository.findByIdAndUserIdForUpdate(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));

        if (interview.getStatus() != PENDING
                && interview.getStatus() != GENERATING
                && interview.getStatus() != ACTIVE) {
            throw new CustomApiException(
                    INVALID_INTERVIEW_STATUS.getHttpStatus(),
                    INVALID_INTERVIEW_STATUS,
                    INVALID_INTERVIEW_STATUS.getMessage()
            );
        }

        if (interview.isQuestionGenerated()) {
            return InterviewGenerateContext.generated();
        }

        if (interview.getStatus() == PENDING) {
            interview.markGenerating();
        }

        UserInterviewSetting byUserId = userInterviewSettingService.findByUserId(userId);
        List<Interview> interviews = interviewRepository.findTop30ByUserIdAndTrackOrderByCreatedAtDesc(userId,byUserId.getTrack());
        List<Long> interviewIds = interviews.stream()
                .map(Interview::getId)
                .toList();

        List<String> primaryTags = interviewIds.isEmpty()
                ? List.of()
                : interviewQuestionRepository.findTop30ByInterviewIdInAndPrimaryTagIsNotNullOrderByCreatedAtDesc(interviewIds)
                  .stream()
                  .map(InterviewQuestion::getPrimaryTag)
                  .distinct()
                  .toList();

        return InterviewGenerateContext.of(
                interview.getTrack(),
                interview.getDifficulty(),
                interview.getInterviewMode(),
                interview.getAnswerTimeSeconds(),
                interview.getCount(),
                primaryTags
        );
    }

    @Transactional
    public void completeGenerate(Long interviewId, Long userId, GeneratedQuestion generatedQuestion) {
        Interview interview = interviewRepository.findByIdAndUserIdForUpdate(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));

        if (interview.isQuestionGenerated()) {
            return;
        }

        List<InterviewQuestion> entities = new ArrayList<>();
        OffsetDateTime now = OffsetDateTime.now();

        for (GeneratedQuestion.Item q : generatedQuestion.questions()) {
            entities.add(InterviewQuestion.createInterviewQuestion(
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

        interviewQuestionRepository.saveAll(entities);
        interview.markGenerated();
    }

    @Transactional
    public void failGenerate(Long interviewId, Long userId, String errorMessage) {
        Interview interview = interviewRepository.findByIdAndUserIdForUpdate(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));

        interview.markGenerateFailed(errorMessage);
    }

    @Transactional(readOnly = true)
    public InterviewQuestionReadResponse getQuestions(Long interviewId, Long userId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));

        if (!interview.getUserId().equals(userId)) {
            throw new CustomApiException(
                    INTERVIEW_FORBIDDEN.getHttpStatus(),
                    INTERVIEW_FORBIDDEN,
                    INTERVIEW_FORBIDDEN.getMessage()
            );
        }

        return InterviewQuestionMapper.fromList(
                interviewQuestionRepository.findAllByInterviewIdOrderBySeqAsc(interviewId),
                false,
                interview
        );
    }

    @Transactional
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
        return interviewRepository.save(
                Interview.reInterviewCreate(idempotencyKey, userId, sourceInterview)
        );
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

    private List<InterviewQuestion> copyBaseQuestions(Interview retryInterview, List<InterviewQuestion> sourceQuestions) {
        return sourceQuestions.stream()
                .map(source -> InterviewQuestion.createInterviewQuestion(
                        retryInterview,
                        source.getSeq(),
                        source.getTitle(),
                        source.getPrimaryTag(),
                        source.getTags() == null ? new LinkedHashSet<>() : new LinkedHashSet<>(source.getTags()),
                        source.getQuestionText(),
                        source.getProvider(),
                        source.getModel(),
                        source.getPromptVersion(),
                        source.getTemperature(),
                        OffsetDateTime.now()
                ))
                .toList();
    }
}