package com.mockio.common_ai_contractor.generator.deepdive;

import com.mockio.common_ai_contractor.constant.AiEngine;

public interface DeepDiveQuestionValid {
    AiEngine engine();
    DeepDiveValid generateValid(GenerateDeepDiveCommand command);
}