package com.mockio.feedback_service.dto.response;

import java.util.List;

public record SummaryFeedbackText(
        Integer totalScore,
        String summaryText,
        List<String> strengths,
        List<String> improvements,
        FeedbackDimensions dimensions
) {}
