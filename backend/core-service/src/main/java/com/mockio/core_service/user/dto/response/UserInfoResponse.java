package com.mockio.core_service.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 사용자 정보 응답 DTO
 * @param id
 * @param email
 * @param nickname
 */

public record UserInfoResponse(

        @Schema(description = "사용자 ID" , example = "1")
        Long id,

        @Schema(description = "이메일" , example = "test@naver.com")
        String email,

        @Schema(description = "닉네임" , example = "호호")
        String nickname

) {}
