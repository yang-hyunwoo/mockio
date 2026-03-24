package com.mockio.core_service.feedback.kafka.dto.request;

public record InterviewAnswerSkippedPayload(
        Long interviewId,
        Long questionId,
        Long answerId
) {}
