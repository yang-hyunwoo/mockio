package com.mockio.common_ai_contractor.generator.feedback;

import java.util.List;

public record GeneratedSummaryFeedback(
        Long interviewId,
        String summaryText,
        Integer totalScore,
        String strengths,
        String improvements,
        String provider,
        String model,
        String promptVersion,
        Double temperature
) {}