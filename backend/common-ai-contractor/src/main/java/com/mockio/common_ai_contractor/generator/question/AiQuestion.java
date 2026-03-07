package com.mockio.common_ai_contractor.generator.question;

import java.util.Set;

public record AiQuestion(
        String title,
        String body,
        Set<String> tags
) {}