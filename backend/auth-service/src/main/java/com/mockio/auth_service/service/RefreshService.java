package com.mockio.auth_service.service;

/**
 * Refresh Token을 이용한 Access Token 재발급을 담당하는 서비스.
 *
 * <p>KeycloakTokenClient를 통해
 * 외부 인증 서버(Keycloak)의 토큰 갱신 기능을 호출하며,
 * 컨트롤러와 외부 API 호출 계층 사이의 중계 역할을 수행한다.</p>
 */

import com.mockio.auth_service.client.KeycloakTokenClient;
import com.mockio.auth_service.dto.response.KeycloakTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshService {

    private final KeycloakTokenClient keycloakTokenClient;

    /**
     * Refresh Token을 사용하여 새로운 토큰을 발급받는다.
     *
     * @param refreshToken 사용자 Refresh Token
     * @return Keycloak에서 발급된 토큰 응답
     */
    public KeycloakTokenResponse refreshBy(String refreshToken) {
        return keycloakTokenClient.refresh(refreshToken);
    }
}
