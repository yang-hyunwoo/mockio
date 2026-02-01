package com.mockio.common_ai_contractor.generator.feedback;

public record GenerateFeedbackCommand(
        String questionText,
        String answerText,
        String track,
        String difficulty,
        String feedbackStyle
) {}

