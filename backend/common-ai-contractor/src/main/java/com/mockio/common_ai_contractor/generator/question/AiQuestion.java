package com.mockio.common_ai_contractor.generator.question;

import java.util.List;

public record AiQuestion(
        String title,
        String body,
        List<String> tags
) {}