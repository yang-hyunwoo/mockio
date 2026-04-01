package com.mockio.auth_service.dto.response;

/**
 * 로그인 응답 DTO
 */

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(

        @Schema(name = "사용자_ID", example = "1")
        Long id,

        @Schema(name = "이메일", example = "test@google.com")
        String email,

        @Schema(name = "엑세스 토큰", example = "eyasdfasdf")
        String accessToken

) {}
