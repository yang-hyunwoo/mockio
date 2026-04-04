package com.mockio.core_service.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 유저 프로필 조회 응답 DTO
 */

public record MyPageProfileDetailResponse(

        @Schema(description = "닉네임" ,example = "호호")
        String nickname,

        @Schema(description = "이메일" ,example = "test@naver.com")
        String email,

        @Schema(description = "프로필ID" ,example = "1")
        Long profileImageId,

        @Schema(description = "프로필URL" ,example = "http://")
        String profileImageUrl

) {}
