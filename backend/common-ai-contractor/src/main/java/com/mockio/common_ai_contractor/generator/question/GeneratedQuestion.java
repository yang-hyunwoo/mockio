package com.mockio.common_ai_contractor.generator.question;

import java.util.List;

public record GeneratedQuestion(
       List<Item> questions
) {
    public record Item(
            int seq,
            String title,
            String body,
            List<String> tags,
            String provider,
            String model,
            String promptVersion,
            Double temperature
    ) {}
}