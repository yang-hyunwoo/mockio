package com.mockio.feedback_service.dto.response;

import java.util.List;

public record InterviewScoreListResponse(
       List<Item> scoreList

) {
    public record Item(
            Long interviewId,
            int score,
            int structure,
            int clarity,
            int specificity
    ) {}
}
