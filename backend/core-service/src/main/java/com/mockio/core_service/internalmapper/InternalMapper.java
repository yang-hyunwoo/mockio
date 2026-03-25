package com.mockio.core_service.internalmapper;


import com.mockio.core_service.feedback.dto.response.*;
import com.mockio.core_service.feedback.kafka.dto.response.InterviewAnswerDetailResponse;
import com.mockio.core_service.interview.dto.request.InternalEnsureInterviewSettingRequest;
import com.mockio.core_service.interview.dto.response.*;
import com.mockio.core_service.interview.kafka.dto.response.InternalInterviewAnswerDetailResponse;
import com.mockio.core_service.user.dto.request.EnsureInterviewSettingRequest;
import com.mockio.core_service.user.dto.response.UserInterviewSettingReadResponse;

import java.util.List;


public class InternalMapper {

    /**
     * interview -> user
     * @param response
     * @return
     */
    public static UserInterviewSettingReadResponse fromInternalUserInterviewSettingRead(
            InterviewUserInterviewSettingReadResponse response
    ) {
        if (response == null) {
            return null;
        }
        return new UserInterviewSettingReadResponse(
                response.id(),
                response.track(),
                response.difficulty(),
                response.feedbackStyle(),
                response.interviewMode(),
                response.answerTimeSeconds(),
                response.questionCount()
        );
    }

    /**
     * user -> interview
     * @param request
     * @return
     */
    public static InternalEnsureInterviewSettingRequest toInternalEnsureInterviewSetting(
            EnsureInterviewSettingRequest request
    ) {
        return new InternalEnsureInterviewSettingRequest(
                request.userId()
        );
    }

    private static FeedbackDimensions fromFeedbackDimensions(
            InternalFeedbackDimensions response
    ) {
        if (response == null) {
            return null;
        }
        return new FeedbackDimensions(
                response.structure(),
                response.clarity(),
                response.specificity()
        );
    }

    private static List<FeedbackDimensions> fromFeedbackDimensionsList(
            List<InternalFeedbackDimensions> response
    ) {
        return response.stream()
                .map(InternalMapper::fromFeedbackDimensions)
                .toList();
    }


    public static FeedbackDetailResponse fromInternalFeedbackDetail(
            InternalFeedbackDetailResponse response
    ) {
        if (response == null) {
            return null;
        }
        return new FeedbackDetailResponse(
                response.id(),
                response.answerId(),
                response.score(),
                response.summary(),
                response.strengths(),
                response.improvements(),
                response.modelAnswer(),
                response.status(),
                InternalMapper.fromFeedbackDimensions(response.dimensions()),
                response.headline(),
                response.improvementTags()
        );
    }

    private static List<FeedbackDetailResponse> fromInternalFeedbackDetailList(
            List<InternalFeedbackDetailResponse> response
    ) {
        return response.stream()
                .map(InternalMapper::fromInternalFeedbackDetail)
                .toList();
    }

    public static FeedbackTotalDetailResponse fromInternalFeedbackTotalDetail(
            InternalFeedbackTotalDetailResponse response
    ) {
        if (response == null) {
            return null;
        }
        return new FeedbackTotalDetailResponse(
                fromInternalSummaryFeedback(response.summaryFeedback()),
                InternalMapper.fromInternalFeedbackDetailList(response.feedbacks())
        );
    }

    private static SummaryFeedback fromInternalSummaryFeedback(
            InternalSummaryFeedback response
    ) {
        if (response == null) {
            return null;
        }
        return new SummaryFeedback(
                response.id(),
                response.totalScore(),
                response.summaryText(),
                response.strengths(),
                response.improvements(),
                response.status(),
                InternalMapper.fromFeedbackDimensions(response.feedbackDimensions())

        );
    }

    public static InterviewScoreListResponse fromInterviewScoreList(
            InternalInterviewScoreListResponse response
    ) {
        if (response == null) {
            return null;
        }
        return new InterviewScoreListResponse(
                fromInterviewScoreListItem(response.scoreList()
                ));
    }

    private static List<InterviewScoreListItem> fromInterviewScoreListItem(
            List<InternalInterviewScoreItem> response
    ) {
        return response.stream()
                .map(InternalMapper::fromInterviewScoreItem)
                .toList();
    }

    private static InterviewScoreListItem fromInterviewScoreItem(
            InternalInterviewScoreItem response
    ) {
        if (response == null) {
            return null;
        }
        return new InterviewScoreListItem(
                response.interviewId(),
                response.score(),
                response.structure(),
                response.clarity(),
                response.specificity()
        );
    }

    public static List<InterviewAnswerDetailResponse> fromInternalInterviewAnswerDetailList(
            List<InternalInterviewAnswerDetailResponse> response
    ) {
        return response.stream()
                .map(InternalMapper::fromInternalInterviewAnswerDetail)
                .toList();
    }

    private static InterviewAnswerDetailResponse fromInternalInterviewAnswerDetail(
            InternalInterviewAnswerDetailResponse response
    ) {
        if (response == null) {
            return null;
        }
        return new InterviewAnswerDetailResponse(
                response.answerId(),
                response.interviewId(),
                response.questionId(),
                response.attempt(),
                response.questionText(),
                response.answerText(),
                response.answerDurationSeconds()
        );
    }

}
