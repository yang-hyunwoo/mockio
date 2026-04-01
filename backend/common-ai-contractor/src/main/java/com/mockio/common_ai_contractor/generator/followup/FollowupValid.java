package com.mockio.common_ai_contractor.generator.followup;

public record FollowupValid(
        boolean shouldFollowUp,
        String reason,
        String focus
) {}
