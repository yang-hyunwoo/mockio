package com.mockio.common_ai_contractor.generator.feedback;

public interface FeedbackGenerator {
    GeneratedFeedback generate(GenerateFeedbackCommand command);
}
