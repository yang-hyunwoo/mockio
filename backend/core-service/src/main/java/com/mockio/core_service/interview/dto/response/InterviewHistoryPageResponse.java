package com.mockio.core_service.interview.dto.response;

public record InterviewHistoryPageResponse(
        InterviewScoreHistoryResponse scoreSection,
        InterviewHistoryResponse historySection,
        WeakPointResponse weakPoints,
        int number,
        int totalPages,
        Long totalElements
) {
}
