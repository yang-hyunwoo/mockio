package com.mockio.interview_service.dto.request;

public record StartInterviewRequest(
        String idempotencyKey
) {
}
