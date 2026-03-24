package com.mockio.core_service.feedback.kafka.dto.request;

public record InterviewCompletedPayload(
        Long userId,
        Long interviewId,
        String track,
        String difficulty,
        String feedbackStyle
) {}