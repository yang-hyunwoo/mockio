package com.mockio.common_ai_contractor.generator.question;

import java.util.List;
import java.util.Set;

public record GeneratedQuestion(
       List<Item> questions
) {
    public record Item(
            int seq,
            String title,
            String body,
            Set<String> tags,
            String provider,
            String model,
            String promptVersion,
            Double temperature
    ) {}
}