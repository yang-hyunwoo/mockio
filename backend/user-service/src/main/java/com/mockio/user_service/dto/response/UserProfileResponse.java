package com.mockio.user_service.dto.response;

import com.mockio.common_spring.util.response.EnumResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

public record UserProfileResponse(
        @Schema(description = "유저_pk", example = "1")
        Long id,
        @Schema(description = "유저_닉네임", example = "cohouseol")
        String nickname,
        @Schema(description = "유저_이름", example = "홍길동")
        String name,
        @Schema(description = "유저_이메일", example = "ex@example.com")
        String email,
        @Schema(description = "유저_프로필", example = "1")
        Long profileImageId,
        @Schema(description = "유저_설명", example = "안녕하세요")
        String bio,
        @Schema(description = "유저_프로필_공개여부", example = "PUBLIC")
        EnumResponse visibility,
        @Schema(description = "유저_상태", example = "ACTIVE")
        EnumResponse  status,
        @Schema(description = "유저_마지막_로그인", example = "")
        OffsetDateTime lastLoginAt
) {}