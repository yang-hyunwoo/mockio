package com.mockio.ai_service.fallback;

import java.util.List;

public record FallbackQuestion(
        String title,
        String body,
        List<String> tags
) {}