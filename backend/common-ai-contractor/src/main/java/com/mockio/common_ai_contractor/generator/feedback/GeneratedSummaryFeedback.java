package com.mockio.common_ai_contractor.generator.feedback;


public record GeneratedSummaryFeedback(
        Long interviewId,
        String summaryFeedbackText,
        Integer totalScore,
        String provider,
        String model,
        String promptVersion,
        Double temperature
) {}