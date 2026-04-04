package com.mockio.core_service.interview.dto.response;

/**
 * 면접 약점 응답 DTO
 */

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record WeakPointResponse(

        @Schema(description = "면접 약점 리스트 ", example = "[]")
        List<Item> weakPointList
) {
    public record Item (

            @Schema(description = "타입", example = "")
            String type,

            @Schema(description = "라벨", example = "")
            String label,

            @Schema(description = "평점", example = "10")
            int averageScore,

            @Schema(description = "메시지", example = "이게 부족합니다.")
            String message

    ){}
}
