package com.mockio.common_ai_contractor.generator.deepdive;


import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;

public record GeneratedDeepDiveBundle(
        DeepDiveDecision decision,
        FollowUpQuestion question
) {}