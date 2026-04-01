package com.mockio.auth_service.dto.request;

/**
 * oauth 로그인 요청 DTO
 */

import com.mockio.auth_service.constant.AuthProviderEnum;
import io.swagger.v3.oas.annotations.media.Schema;

public record OauthUserRequest(

        @Schema(name = "oauth_email", example = "test@google.com")
        String email,

        @Schema(name = "소셜로그인값", example = "AuthProviderEnum.GOOGLE")
        AuthProviderEnum provider,

        @Schema(name = "비밀번호", example = "asdfgg")
        String password,

        @Schema(name = "닉네임", example = "테스터")
        String nickname,

        @Schema(name = "이름", example = "홍길동")
        String name

) {}
