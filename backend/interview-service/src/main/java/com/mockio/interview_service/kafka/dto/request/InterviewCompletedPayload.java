package com.mockio.interview_service.kafka.dto.request;

public record InterviewCompletedPayload(
        Long interviewId,
        String track,
        String difficulty,
        String feedbackStyle
) {}