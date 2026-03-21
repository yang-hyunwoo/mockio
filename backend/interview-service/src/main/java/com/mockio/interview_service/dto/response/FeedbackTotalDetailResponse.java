package com.mockio.interview_service.dto.response;


import com.mockio.common_spring.util.response.EnumResponse;

import java.util.List;

public record FeedbackTotalDetailResponse(
        SummaryFeedback summaryFeedback,
        List<FeedbackDetailResponse> feedbacks
) {

    public record SummaryFeedback(
            Long id,
            Integer totalScore,
            String summaryText,
            List<String> strengths,
            List<String> improvements,
            EnumResponse status,
            FeedbackDimensions feedbackDimensions

    ) {

    }
}
