package com.mockio.feedback_service.kafka.dto;

public record InterviewAnswerSubmittedPayload(
        Long interviewId,
        Long questionId,
        Long answerId,
        Integer attempt,
        String track,
        String difficulty,
        String feedbackStyle
) {}
