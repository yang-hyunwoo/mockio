package com.mockio.core_service.interview.dto.response;

public record InterviewScoreListItem(
        Long interviewId,
        int score,
        int structure,
        int clarity,
        int specificity
) {
}
