package com.mockio.common_ai_contractor.generator.question;

public record BaseQuestionContext(
        String title,
        String body,
        String primaryTag
) {}
