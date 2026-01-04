package com.mockio.interview_service.generator;

public record GeneratedQuestion(
        int seq,
        String questionText,
        String provider,
        String model,
        String promptVersion,
        Double temperature
) {
}