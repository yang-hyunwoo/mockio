package com.mockio.common_ai_contractor.generator.feedback;

public interface SummaryFeedbackGenerator {
    GeneratedSummaryFeedback generate(GeneratedSummaryFeedbackCommand command);
}
