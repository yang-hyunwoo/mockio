package com.mockio.ai_service.fake;

import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.feedback.FeedbackGenerator;
import com.mockio.common_ai_contractor.generator.feedback.GenerateFeedbackCommand;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedFeedback;
import org.springframework.stereotype.Component;

@Component
public class FakeFeedBackGenerator implements FeedbackGenerator {

    @Override
    public AiEngine engine() {
        return AiEngine.FAKE;
    }


    @Override
    public GeneratedFeedback generate(GenerateFeedbackCommand command) {
        return new GeneratedFeedback(
                "페이크 피드백",
                30,
                "FAKE",
                "FAKE",
                "0.0",
                0.0
        );
    }

}
