package com.mockio.interview_service.generator;

import java.util.List;

public interface InterviewQuestionGenerator {
    List<GeneratedQuestion> generate(GenerateQuestionCommand command);
}
