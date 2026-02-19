package com.mockio.common_ai_contractor.generator.deepdive;

import java.util.List;

public record DeepDiveDecision(
        boolean shouldFollowUp,
        int depth,
        List<String> focus,
        List<String> gaps,
        String reason
) {}
