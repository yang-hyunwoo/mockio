package com.mockio.common_ai_contractor.generator.deepdive;

public record DeepDiveValid(
        boolean shouldDeepDive,
        String reason,
        String focus
) {}
