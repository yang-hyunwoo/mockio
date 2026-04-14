package com.mockio.core_service.ai.fake;

import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.compare.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FakeCompareQuestionGenerator implements CompareQuestionGenerator {
    @Override
    public AiEngine engine() {
        return AiEngine.FAKE;
    }

    @Override
    public GeneratedCompareQuestion generate(GeneratedCompareQuestionCommand command) {
        return new GeneratedCompareQuestion(
            "문구 부적절",
                "문장 부적절",
                List.of("강점"),
                List.of("약점"),
                List.of("부적절"),
                "BETTER",
                "FAKE",
                "Fake",
                "v0.0",
                0.0
        );
    }

}
