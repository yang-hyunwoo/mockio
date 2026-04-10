package com.mockio.core_service.feedback.dto.response;


import java.util.List;

public record SummaryFeedbackText(
        Integer totalScore,
        String summaryText,
        List<String> strengths,
        List<String> improvements,
        InternalFeedbackDimensions dimensions,
        InternalFeedbackJobMetric jobMetrics
) {}
