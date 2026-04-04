package com.mockio.auth_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 로그인 응답 DTO
 */


public record LoginResponse(

        @Schema(description = "사용자_ID", example = "1")
        Long id,

        @Schema(description = "이메일", example = "test@google.com")
        String email,

        @Schema(description = "엑세스 토큰", example = "eyasdfasdf")
        String accessToken

) {}
