package com.mockio.core_service.interview.dto.response;

/**
 * 면접 점수 이력 응답 DTO
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mockio.common_spring.util.response.EnumResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

public record InterviewScoreHistoryResponse(
        @Schema(description = "점수이력리스트", example = "[]")
        List<Item> scoreHistory
) {
    public record Item(

            @Schema(description = "면접ID", example = "1")
            Long interviewId,

            @Schema(description = "제목", example = "제목입니다")
            String title,

            @Schema(description = "면접트랙", example = "HR")
            EnumResponse track,

            @Schema(description = "점수", example = "10")
            int score,

            @Schema(description = "종료일", example = "03.03")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM.dd")
            OffsetDateTime endedAt

    ) {}
}
