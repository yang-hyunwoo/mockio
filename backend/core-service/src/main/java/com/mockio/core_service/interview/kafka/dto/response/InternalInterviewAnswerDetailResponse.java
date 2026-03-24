package com.mockio.core_service.interview.kafka.dto.response;

public record InternalInterviewAnswerDetailResponse(
        Long answerId,
        Long interviewId,
        Long questionId,
        Integer attempt,
        String questionText,
        String answerText,
        Integer answerDurationSeconds
) {}