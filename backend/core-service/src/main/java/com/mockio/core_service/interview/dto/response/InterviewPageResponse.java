package com.mockio.core_service.interview.dto.response;

/**
 * 면접 페이징 목록 조회 응답 DTO
 */


import com.fasterxml.jackson.annotation.JsonFormat;
import com.mockio.common_spring.util.response.EnumResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

public record InterviewPageResponse(

        @Schema(description = "면접 ID" , example = "1")
        Long id,

        @Schema(description = "제목" , example = "제목입니다.")
        String title,

        @Schema(description = "생성일" , example = "2026-01-01")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        OffsetDateTime createdAt,

        @Schema(description = "진행률" , example = "10")
        int progress,

        @Schema(description = "멱등성키" , example = "sdfasdf")
        String idempotencyKey,

        @Schema(description = "총갯수" , example = "10")
        int totalCount,

        @Schema(description = "면접 상태" , example = "ACTIVE")
        EnumResponse status,

        @Schema(description = "면접 트랙" , example = "HR")
        EnumResponse track,

        @Schema(description = "면접 난이도" , example = "EASY")
        EnumResponse difficulty,

        @Schema(description = "면접 피드백" , example = "STRICT")
        EnumResponse feedbackStyle,

        @Schema(description = "면접 종료 사유" , example = "USER_EXIT")
        EnumResponse endReason

){}
