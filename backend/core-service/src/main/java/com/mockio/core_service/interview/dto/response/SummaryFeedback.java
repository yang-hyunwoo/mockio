package com.mockio.core_service.interview.dto.response;

import com.mockio.common_spring.util.response.EnumResponse;

import java.util.List;

public record SummaryFeedback(
        Long id,
        Integer totalScore,
        String summaryText,
        List<String> strengths,
        List<String> improvements,
        EnumResponse status,
        FeedbackDimensions feedbackDimensions
) {}
