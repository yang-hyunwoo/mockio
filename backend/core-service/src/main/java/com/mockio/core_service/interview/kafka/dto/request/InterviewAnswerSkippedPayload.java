package com.mockio.core_service.interview.kafka.dto.request;

public record InterviewAnswerSkippedPayload(
        Long interviewId,
        Long questionId,
        Long answerId
) {}
