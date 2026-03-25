package com.mockio.core_service.interview.dto.request;

public record RetryInterviewRequest(
        String idempotencyKey,
        Long interviewId
) {}
