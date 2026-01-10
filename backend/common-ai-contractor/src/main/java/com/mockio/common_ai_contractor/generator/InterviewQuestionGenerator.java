package com.mockio.common_ai_contractor.generator;

import java.util.List;

public interface InterviewQuestionGenerator {
    List<GeneratedQuestion> generate(GenerateQuestionCommand command);
}
