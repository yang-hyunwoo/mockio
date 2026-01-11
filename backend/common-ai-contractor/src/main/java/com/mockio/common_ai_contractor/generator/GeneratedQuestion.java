package com.mockio.common_ai_contractor.generator;

import java.util.List;

public record GeneratedQuestion(
       List<Item> questions
) {
    public record Item(
            int seq,
            String questionText,
            String provider,
            String model,
            String promptVersion,
            Double temperature
    ) {}
}