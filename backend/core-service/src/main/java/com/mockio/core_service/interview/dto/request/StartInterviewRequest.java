package com.mockio.core_service.interview.dto.request;

public record StartInterviewRequest(
        String idempotencyKey
) {
}
