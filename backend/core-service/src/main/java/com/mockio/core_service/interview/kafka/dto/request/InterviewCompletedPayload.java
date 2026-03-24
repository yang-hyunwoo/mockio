package com.mockio.core_service.interview.kafka.dto.request;

public record InterviewCompletedPayload(
        Long userId,
        Long interviewId,
        String track,
        String difficulty,
        String feedbackStyle
) {}