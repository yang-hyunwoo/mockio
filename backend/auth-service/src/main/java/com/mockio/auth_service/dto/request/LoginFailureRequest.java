package com.mockio.auth_service.dto.request;

/**
 * 로그인 실패 기록 요청 DTO
 */

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginFailureRequest(

        @Schema(name = "이메일", example = "ex@test.com")
        String email

) {}
