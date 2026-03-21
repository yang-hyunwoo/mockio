package com.mockio.feedback_service.mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.feedback_service.domain.InterviewFeedback;
import com.mockio.feedback_service.dto.response.FeedbackDetailResponse;
import com.mockio.feedback_service.dto.response.FeedbackText;

public class FeedbackMapper {

    public static FeedbackDetailResponse from(InterviewFeedback interviewFeedback, FeedbackText feedbackText) {
        return new FeedbackDetailResponse(
                interviewFeedback.getId(),
                interviewFeedback.getAnswerId(),
                interviewFeedback.getScore(),
                feedbackText != null ? feedbackText.summary() : null,
                feedbackText != null ? feedbackText.strengths() : null,
                feedbackText != null ? feedbackText.improvements() : null,
                feedbackText != null ? feedbackText.modelAnswer() : null,
                EnumResponse.of(
                        interviewFeedback.getStatus().name(),
                        interviewFeedback.getStatus().getLabel()
                ),
                feedbackText != null ? feedbackText.dimensions() : null,
                feedbackText != null ? feedbackText.headline() : null,
                feedbackText != null ? feedbackText.improvementTags() : null
        );
    }

}
