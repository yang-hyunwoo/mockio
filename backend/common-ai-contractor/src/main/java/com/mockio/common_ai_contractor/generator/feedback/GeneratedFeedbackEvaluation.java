package com.mockio.common_ai_contractor.generator.feedback;

import java.util.List;

public record GeneratedFeedbackEvaluation(

        Integer score,
        FeedbackDimensions dimensions,
        FeedbackJobMetric jobMetrics,
        String answerType,
        String headline,
        String summary,
        List<String> improvementTags

) {
}
