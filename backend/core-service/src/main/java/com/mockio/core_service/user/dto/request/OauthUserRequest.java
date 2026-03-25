package com.mockio.core_service.user.dto.request;


import com.mockio.core_service.user.constant.AuthProviderEnum;

public record OauthUserRequest(
        String email,
        String name,
        AuthProviderEnum provider,
        String password,
        String nickname
) {}
