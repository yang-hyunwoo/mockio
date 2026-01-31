package com.mockio.interview_service.kafka.dto.request;

public record InterviewAnswerSubmittedPayload(
        Long interviewId,
        Long questionId,
        Long answerId,
        Integer attempt,
        String track,
        String difficulty,
        String feedbackStyle
) {}
