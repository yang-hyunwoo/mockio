package com.mockio.common_ai_contractor.generator;


public interface InterviewQuestionGenerator {
    GeneratedQuestion generate(GenerateQuestionCommand command);
}
