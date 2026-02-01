package com.mockio.common_ai_contractor.generator.feedback;

public record GeneratedFeedback(
        String feedbackText,
        Integer score,
        String provider,
        String model,
        String promptVersion,
        Double temperature
) {}
