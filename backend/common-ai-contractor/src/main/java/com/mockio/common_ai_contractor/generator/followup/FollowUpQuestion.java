package com.mockio.common_ai_contractor.generator.followup;


import java.util.Set;

public record FollowUpQuestion(
        Item questions
) {
    public record Item(
            String title,
            String body,
            Set<String> tags,
            String provider,
            String model,
            String promptVersion,
            Double temperature
    ) {}
}