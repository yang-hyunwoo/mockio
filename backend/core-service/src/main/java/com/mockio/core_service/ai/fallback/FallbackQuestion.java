package com.mockio.core_service.ai.fallback;

import java.util.Set;

public record FallbackQuestion(
        String title,
        String body,
        Set<String> tags
) {}