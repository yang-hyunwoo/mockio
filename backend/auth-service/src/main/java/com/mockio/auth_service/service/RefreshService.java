package com.mockio.auth_service.service;

import com.mockio.auth_service.client.KeycloakTokenClient;
import com.mockio.auth_service.dto.KeycloakTokenResponse;
import com.mockio.common_spring.exception.RefreshTokenInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class RefreshService {

    private final KeycloakTokenClient keycloakTokenClient;


    public KeycloakTokenResponse refreshBy(String refreshToken) {
        return keycloakTokenClient.refresh(refreshToken);
//        try {
//            return keycloakTokenClient.refresh(refreshToken);
//        } catch (HttpClientErrorException e) {
//            String body = e.getResponseBodyAsString();
//            if (body != null && body.contains("invalid_grant")) {
//                throw new RefreshTokenInvalidException("REFRESH_INVALID_GRANT", e);
//            }
//            throw e;
//        }
    }
}
