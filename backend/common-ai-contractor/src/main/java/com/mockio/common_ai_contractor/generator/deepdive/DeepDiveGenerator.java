package com.mockio.common_ai_contractor.generator.deepdive;

import com.mockio.common_ai_contractor.constant.AiEngine;

public interface DeepDiveGenerator {
    AiEngine engine();
    GeneratedDeepDiveBundle generate(GenerateDeepDiveCommand command);
}
