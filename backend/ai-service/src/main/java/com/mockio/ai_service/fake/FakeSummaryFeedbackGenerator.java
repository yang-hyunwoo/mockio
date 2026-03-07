package com.mockio.ai_service.fake;

import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedSummaryFeedback;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedSummaryFeedbackCommand;
import com.mockio.common_ai_contractor.generator.feedback.SummaryFeedbackGenerator;
import org.springframework.stereotype.Component;

@Component
public class FakeSummaryFeedbackGenerator implements SummaryFeedbackGenerator {
    @Override
    public AiEngine engine() {
        return AiEngine.FAKE;
    }

    @Override
    public GeneratedSummaryFeedback generate(GeneratedSummaryFeedbackCommand command) {
        return null;
    }
}
