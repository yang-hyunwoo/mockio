package com.mockio.interview_service.kafka.dto.request;

public record InterviewAnswerSkippedPayload(
        Long interviewId,
        Long questionId,
        Long answerId
) {}
