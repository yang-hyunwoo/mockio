package com.mockio.auth_service.oauth.provider;

import com.mockio.auth_service.constant.AuthProviderEnum;

import java.util.Map;

import static com.mockio.auth_service.constant.AuthProviderEnum.*;


public class NaverUserInfo implements OAuth2UserInfo{

    private Map<String , Object> attributes; // oauth2User.getAttributes() 받기

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public AuthProviderEnum getProvider() {
        return NAVER;
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

}
