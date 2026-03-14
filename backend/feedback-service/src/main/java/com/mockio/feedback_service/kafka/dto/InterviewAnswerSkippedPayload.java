package com.mockio.feedback_service.kafka.dto;

public record InterviewAnswerSkippedPayload(
        Long interviewId,
        Long questionId,
        Long answerId
) {}
