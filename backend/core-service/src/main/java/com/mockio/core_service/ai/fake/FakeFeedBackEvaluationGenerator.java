package com.mockio.core_service.ai.fake;

import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.feedback.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FakeFeedBackEvaluationGenerator implements FeedbackEvaluationGenerator {

    @Override
    public AiEngine engine() {
        return AiEngine.FAKE;
    }


    @Override
    public GeneratedFeedbackEvaluation generate(GenerateFeedbackCommand command) {

        return new GeneratedFeedbackEvaluation(
                50,
                new FeedbackDimensions(
                        20,
                        30,
                        40
                ),
                new FeedbackJobMetric(
                        40,
                        50,
                        60
                ),
                "FAKE",
                "내용이 부족합니다.",
                "질문 의도를 충분히 충족하지 못했습니다.",
                List.of("사례 부족")
        );
    }
}
