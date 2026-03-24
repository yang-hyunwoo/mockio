package com.mockio.core_service.interview.kafka.dto.request;

public record InterviewAnswerSubmittedPayload(
        Long interviewId,
        Long questionId,
        String questionText,
        Long answerId,
        String answerText,
        Integer attempt,
        String track,
        String difficulty,
        String feedbackStyle
) {}
