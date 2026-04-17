package com.mockio.support_service.questionboard.dto.internal.response;

/**
 * 면접 메인 목록 조회 응답 DTO
 */

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record InterviewListResponse(

        @Schema(description = "면접 목록 리스트" , example = "[]")
        List<Item> interviews
){
    public record Item(

            @Schema(description = "면접 ID" , example = "1")
            Long id,

            @Schema(description = "제목" , example = "제목입니다.")
            String title,

            @Schema(description = "생성일" , example = "2026-01-01")
            LocalDate createdAt,

            @Schema(description = "진행률" , example = "30")
            int progress

    ) {}
}
