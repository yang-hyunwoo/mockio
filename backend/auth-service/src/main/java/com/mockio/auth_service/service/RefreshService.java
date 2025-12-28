package com.mockio.auth_service.service;

import com.mockio.auth_service.client.KeycloakTokenClient;
import com.mockio.auth_service.dto.response.KeycloakTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshService {

    private final KeycloakTokenClient keycloakTokenClient;


    public KeycloakTokenResponse refreshBy(String refreshToken) {
        return keycloakTokenClient.refresh(refreshToken);
    }
}
