package com.mockio.core_service.interview.dto.response;

/**
 * 면접 이력 리스트 응답 DTO
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mockio.common_spring.util.response.EnumResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

public record InterviewHistoryResponse(
        @Schema(description = "면접 점수 이력 DTO", example = "InterviewScoreHistoryResponse")
        List<Item> historyItems
) {
    public record Item(

            @Schema(description = "면접ID", example = "InterviewScoreHistoryResponse")
            Long interviewId,

            @Schema(description = "제목", example = "제목입니다")
            String title,

            @Schema(description = "질문갯수", example = "3")
            int questionCount,

            @Schema(description = "면접상태", example = "END")
            EnumResponse status,

            @Schema(description = "종료이유", example = "사용자 종료")
            EnumResponse endReason,

            @Schema(description = "면접 트랙", example = "HR")
            EnumResponse track,

            @Schema(description = "면접 점수", example = "10")
            int score,

            @Schema(description = "면접 생성일", example = "2026-01-01")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            OffsetDateTime createdAt
    ) {}
}
