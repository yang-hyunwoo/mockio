package com.mockio.interview_service.dto.response;

import java.util.List;

public record InterviewHistoryPageResponse(
        InterviewScoreHistoryResponse scoreSection,
        InterviewHistoryResponse historySection,
        WeakPointResponse weakPoints,
        int number,
        int totalPages,
        Long totalElements
) {
}
