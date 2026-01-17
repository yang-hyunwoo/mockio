package com.mockio.common_ai_contractor.generator;

public interface FollowUpQuestionGenerator {
    FollowUpQuestion generate(FollowUpQuestionCommand command);
}