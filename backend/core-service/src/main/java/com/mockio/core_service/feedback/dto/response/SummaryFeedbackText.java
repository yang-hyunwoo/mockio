package com.mockio.core_service.feedback.dto.response;


import com.mockio.common_ai_contractor.generator.feedback.FeedbackJobMetric;

import java.util.List;

public record SummaryFeedbackText(
        Integer totalScore,
        String summaryText,
        List<String> strengths,
        List<String> improvements,
        InternalFeedbackDimensions dimensions,
        FeedbackJobMetric jobMetrics
) {}
