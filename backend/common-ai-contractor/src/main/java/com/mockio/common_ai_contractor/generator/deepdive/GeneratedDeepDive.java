package com.mockio.common_ai_contractor.generator.deepdive;

public record GeneratedDeepDive(
        String question,
        String provider,
        String model,
        String promptVersion,
        Double temperature
) {}
