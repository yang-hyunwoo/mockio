package com.mockio.feedback_service.kafka.dto;

public record InterviewAnswerSubmittedPayload(
        Long interviewId,
        Long questionId,
        String questionText,
        Long answerId,
        String answerText,
        Integer attempt,
        String track,
        String difficulty,
        String feedbackStyle
) {}
