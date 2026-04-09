package com.mockio.core_service.interview.dto.response;

import java.util.Set;

public record SelectedQuestion(
        String title,
        String body,
        String primaryTag,
        Set<String> tags,
        String provider,
        String model,
        String promptVersion,
        Double temperature

) {}
