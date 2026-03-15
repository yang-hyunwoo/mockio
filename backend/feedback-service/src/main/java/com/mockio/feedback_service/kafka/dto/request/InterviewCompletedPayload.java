package com.mockio.feedback_service.kafka.dto.request;

public record InterviewCompletedPayload(
        Long userId,
        Long interviewId,
        String track,
        String difficulty,
        String feedbackStyle
) {}