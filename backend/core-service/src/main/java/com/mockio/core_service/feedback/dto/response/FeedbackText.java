package com.mockio.core_service.feedback.dto.response;


import com.mockio.common_ai_contractor.generator.feedback.FeedbackJobMetric;

import java.util.List;

public record FeedbackText(
        Integer score,
        InternalFeedbackDimensions dimensions,
        String headline,
        String summary,
        List<String> strengths,
        List<InternalFeedbackImprovement> improvements,
        List<String> improvementTags,
        String modelAnswer,
        FeedbackJobMetric jobMetrics
) {}

