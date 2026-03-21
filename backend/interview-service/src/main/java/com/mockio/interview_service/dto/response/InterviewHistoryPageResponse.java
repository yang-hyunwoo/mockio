package com.mockio.interview_service.dto.response;

public record InterviewHistoryPageResponse(
        InterviewScoreHistoryResponse scoreHistory ,
        InterviewHistoryResponse historyItems,
        int number,
        int totalPages,
        Long totalElements
) {
}
