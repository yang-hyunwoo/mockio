package com.mockio.interview_service.dto.request;

public record RetryInterviewRequest(
        String idempotencyKey,
        Long interviewId
) {
}
