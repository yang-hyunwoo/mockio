package com.mockio.common_ai_contractor.generator;

public record GeneratedQuestion(
        int seq,
        String questionText,
        String provider,
        String model,
        String promptVersion,
        Double temperature
) {
}