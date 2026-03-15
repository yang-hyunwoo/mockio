package com.mockio.feedback_service.kafka.dto.request;

public record InterviewAnswerSkippedPayload(
        Long interviewId,
        Long questionId,
        Long answerId
) {}
