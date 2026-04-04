package com.mockio.core_service.interview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 면접 이력 페이징 응답 DTO
 */

public record InterviewHistoryPageResponse(

        @Schema(description = "면접 점수 이력 DTO", example = "InterviewScoreHistoryResponse")
        InterviewScoreHistoryResponse scoreSection,

        @Schema(description = "면접 이력 DTO", example = "InterviewHistoryResponse")
        InterviewHistoryResponse historySection,

        @Schema(description = "약점 DTO", example = "WeakPointResponse")
        WeakPointResponse weakPoints,

        @Schema(description = "페이징 넘버", example = "1")
        int number,

        @Schema(description = "총 페이지", example = "2")
        int totalPages,

        @Schema(description = "총 갯수", example = "3")
        Long totalElements

) {}
