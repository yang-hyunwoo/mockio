package com.mockio.common_spring.util;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * API 에러 응답 DTO
 */

public record APIErrorResponse(

        @Schema(name = "코드", example = "4xx")
        String resultCode,

        @Schema(name = "http코드", example = "4xx")
        Integer httpCode,

        @Schema(name = "메시지", example = "실패")
        String message,

        @Schema(name = "에러코드", example = "4xx")
        String errCode,

        @Schema(name = "에러메시지", example = "실패")
        String errCodeMsg,

        @Schema(name = "데이터", example = "null")
        Object data,

        @Schema(name = "시간", example = "now")
        String timestamp

) {}
