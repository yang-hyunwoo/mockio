package com.mockio.feedback_service.kafka.dto.response;

public record GeneratedFeedback(
        String feedbackText,
        Integer score,
        String provider,
        String model,
        String promptVersion,
        Double temperature
) {}
