package com.mockio.common_ai_contractor.generator.feedback;


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