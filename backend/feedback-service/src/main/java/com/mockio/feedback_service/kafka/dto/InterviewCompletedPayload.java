package com.mockio.feedback_service.kafka.dto;

public record InterviewCompletedPayload(
        Long interviewId,
        String track,
        String difficulty,
        String feedbackStyle
) {}