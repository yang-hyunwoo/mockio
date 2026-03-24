package com.mockio.core_service.feedback.mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.core_service.feedback.domain.InterviewFeedback;
import com.mockio.core_service.feedback.dto.response.InternalFeedbackDetailResponse;
import com.mockio.core_service.feedback.dto.response.FeedbackText;

public class FeedbackMapper {

    public static InternalFeedbackDetailResponse from(InterviewFeedback interviewFeedback, FeedbackText feedbackText) {
        return new InternalFeedbackDetailResponse(
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
