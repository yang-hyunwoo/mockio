package com.mockio.common_ai_contractor.generator.question;


import com.mockio.common_ai_contractor.constant.AiEngine;

public interface InterviewQuestionGenerator {
    AiEngine engine();
    GeneratedQuestion generate(GenerateQuestionCommand command);
}
