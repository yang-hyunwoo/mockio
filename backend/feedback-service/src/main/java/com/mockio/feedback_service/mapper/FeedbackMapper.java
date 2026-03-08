package com.mockio.feedback_service.mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.feedback_service.domain.InterviewFeedback;
import com.mockio.feedback_service.dto.response.FeedbackDetailResponse;
import com.mockio.feedback_service.dto.response.FeedbackText;

public class FeedbackMapper {

    public static FeedbackDetailResponse from(InterviewFeedback interviewFeedback, FeedbackText feedbackText) {
        return new FeedbackDetailResponse(
                interviewFeedback.getId(),
                interviewFeedback.getScore(),
                feedbackText.summary(),
                feedbackText.strengths(),
                feedbackText.improvements(),
                feedbackText.modelAnswer(),
                EnumResponse.of(
                        interviewFeedback.getStatus().name(),
                        interviewFeedback.getStatus().getLabel()
                )
        );
    }

}
