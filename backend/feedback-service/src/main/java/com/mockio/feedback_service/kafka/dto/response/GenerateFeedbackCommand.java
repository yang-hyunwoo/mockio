package com.mockio.feedback_service.kafka.dto.response;

public record GenerateFeedbackCommand(
        String questionText,
        String answerText,
        String track,
        String difficulty,
        String feedbackStyle
) {}

