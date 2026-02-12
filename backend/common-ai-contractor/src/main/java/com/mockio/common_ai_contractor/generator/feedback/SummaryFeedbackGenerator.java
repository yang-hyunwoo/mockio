package com.mockio.common_ai_contractor.generator.feedback;

import com.mockio.common_ai_contractor.constant.AiEngine;

public interface SummaryFeedbackGenerator {
    AiEngine engine();
    GeneratedSummaryFeedback generate(GeneratedSummaryFeedbackCommand command);
}
