package com.mockio.core_service.ai.fake;

import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveCommand;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveQuestionValid;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveValid;
import com.mockio.common_ai_contractor.generator.deepdive.GenerateDeepDiveCommand;
import org.springframework.stereotype.Component;

@Component
public class FakeDeepDiveQuestionValid implements DeepDiveQuestionValid {

    @Override
    public AiEngine engine() {
        return AiEngine.FAKE;
    }

    @Override
    public DeepDiveValid generateValid(GenerateDeepDiveCommand command) {
        return new DeepDiveValid(
                true,
                "페이크_생성",
                "페이크 생성"
        );
    }

}
