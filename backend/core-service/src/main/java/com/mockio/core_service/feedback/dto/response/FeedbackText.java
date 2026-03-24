package com.mockio.core_service.feedback.dto.response;


import java.util.List;

public record FeedbackText(
        Integer score,
        InternalFeedbackDimensions dimensions,
        String headline,
        String summary,
        List<String> strengths,
        List<String> improvements,
        List<String> improvementTags,
        String modelAnswer
) {}

