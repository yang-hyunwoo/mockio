package com.mockio.user_service.dto.request;

import com.mockio.user_service.constant.ProfileVisibility;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserProfileUpdateRequest(
        @Schema(description = "유저_닉네임", example = "cohouseol")
        String nickname,
        @Schema(description = "유저_프로필", example = "1")
        Long profileImageId,
        @Schema(description = "유저_설명", example = "안녕하세요")
        String bio,
        @Schema(description = "유저_프로필_공개여부", example = "PUBLIC")
        ProfileVisibility visibility
) {}
