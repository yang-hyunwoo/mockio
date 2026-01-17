package com.mockio.common_ai_contractor.generator;


import java.util.List;

public record FollowUpQuestion(
        Item questions
) {
    public record Item(
            String questionText,
            String provider,
            String model,
            String promptVersion,
            Double temperature
    ) {
    }
}