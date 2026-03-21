package com.mockio.feedback_service.dto.response;

import java.util.List;

public record FeedbackText(
        Integer score,
        FeedbackDimensions dimensions,
        String headline,
        String summary,
        List<String> strengths,
        List<String> improvements,
        List<String> improvementTags,
        String modelAnswer
) {}

