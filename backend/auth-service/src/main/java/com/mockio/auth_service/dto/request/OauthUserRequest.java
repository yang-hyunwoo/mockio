package com.mockio.auth_service.dto.request;

import com.mockio.auth_service.constant.AuthProviderEnum;

public record OauthUserRequest(
        String email,
        AuthProviderEnum provider,
        String password,
        String nickname,
        String name
) {
}
