package com.mockio.core_service.ai.fallback;

import com.mockio.common_ai_contractor.generator.question.Question;

import java.util.Set;

public record FallbackQuestion(
        Question question,
        String primaryTag,
        Set<String> tags
) {}
