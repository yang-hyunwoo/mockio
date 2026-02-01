package com.mockio.common_ai_contractor.generator.followup;

public interface FollowUpQuestionGenerator {
    FollowUpQuestion generate(FollowUpQuestionCommand command);
}