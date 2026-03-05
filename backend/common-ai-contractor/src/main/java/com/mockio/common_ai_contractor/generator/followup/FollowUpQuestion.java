package com.mockio.common_ai_contractor.generator.followup;


import java.util.List;

public record FollowUpQuestion(
        Item questions
) {
    public record Item(
            String title,
            String body,
            List<String> tags,
            String provider,
            String model,
            String promptVersion,
            Double temperature
    ) {}
}