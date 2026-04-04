package com.mockio.auth_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 사용자 상세 정보 응답 DTO
 */

public record UserInfoResponse(

        @Schema(description = "사용자_ID", example = "1")
        Long id,

        @Schema(description = "이메일", example = "test@test.com")
        String email,

        @Schema(description = "닉네임", example = "히하")
        String nickname

) {}
