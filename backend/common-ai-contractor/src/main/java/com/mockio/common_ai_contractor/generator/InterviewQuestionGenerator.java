package com.mockio.common_ai_contractor.generator;

import java.util.List;

public interface InterviewQuestionGenerator {
    GeneratedQuestion generate(GenerateQuestionCommand command);
}
