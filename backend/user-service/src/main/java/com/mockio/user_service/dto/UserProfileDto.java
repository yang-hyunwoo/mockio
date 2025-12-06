package com.mockio.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserProfileDto(
        @Schema(description = "유저_pk", example = "1")
        Long id,
        @Schema(description = "keycloak_Pk", example = "1")
        String keycloakId,
        @Schema(description = "유저_프로필_이미지_pk", example = "1")
        Long profileImageId,
        @Schema(description = "유저_이름", example = "홍길동")
        String name,
        @Schema(description = "유저_이메일", example = "hong@example.com")
        String email,
        @Schema(description = "유저_닉네임", example = "gusdn")
        String nickname,
        @Schema(description = "유저_휴대폰번호", example = "01012345678")
        String phoneNumber
) {

}
