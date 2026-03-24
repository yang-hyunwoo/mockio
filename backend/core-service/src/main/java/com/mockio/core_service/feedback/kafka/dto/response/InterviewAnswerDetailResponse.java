package com.mockio.core_service.feedback.kafka.dto.response;

public record InterviewAnswerDetailResponse(
        Long answerId,
        Long interviewId,
        Long questionId,
        Integer attempt,
        String questionText,
        String answerText,
        Integer answerDurationSeconds
) {}