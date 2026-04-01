package com.mockio.auth_service.oauth.provider;

/**
 * 소셜 사용자 정보 인터페이스
 */

import com.mockio.auth_service.constant.AuthProviderEnum;

public interface OAuth2UserInfo {

    String getProviderId();

    AuthProviderEnum getProvider();

    String getEmail();

    String getName();

}
