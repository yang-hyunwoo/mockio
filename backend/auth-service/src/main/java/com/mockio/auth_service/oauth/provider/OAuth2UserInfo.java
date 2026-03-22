package com.mockio.auth_service.oauth.provider;

import com.mockio.auth_service.constant.AuthProviderEnum;

public interface OAuth2UserInfo {

    String getProviderId();

    AuthProviderEnum getProvider();

    String getEmail();

    String getName();

}
