package com.mockio.ai_service.fallback;

import java.util.Set;

public record FallbackQuestion(
        String title,
        String body,
        Set<String> tags
) {}