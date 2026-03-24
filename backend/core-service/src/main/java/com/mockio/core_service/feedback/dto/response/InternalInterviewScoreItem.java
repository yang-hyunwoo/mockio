package com.mockio.core_service.feedback.dto.response;

public record InternalInterviewScoreItem(
        Long interviewId,
        int score,
        int structure,
        int clarity,
        int specificity
) {
}
