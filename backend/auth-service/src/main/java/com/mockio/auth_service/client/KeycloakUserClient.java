package com.mockio.auth_service.client;

/**
 * Keycloak Admin API를 통해 사용자 계정을 관리하는 클라이언트 컴포넌트.
 *
 * <p>관리자(Client Credentials) 토큰을 사용하여
 * Keycloak 사용자 활성/비활성 등 관리 작업을 수행한다.</p>
 *
 * <p>외부 인증 인프라(Keycloak) 장애가 애플리케이션 전반으로
 * 전파되지 않도록 Circuit Breaker를 통해 보호된다.</p>
 */

import com.mockio.auth_service.dto.request.UserUpdateRequest;
import com.mockio.auth_service.dto.response.TokenResponse;
import com.mockio.common_spring.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import static com.mockio.common_spring.constant.CommonErrorEnum.ILLEGALSTATE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.*;

@Component
@RequiredArgsConstructor
public class KeycloakUserClient {

    private final RestClient restClient;

    @Value("${keycloak.base-url}")
    private String baseUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.admin.client-secret}")
    private String clientSecret;

    /**
     * Keycloak Admin API 호출을 위한 관리자 Access Token을 발급받는다.
     *
     * <p>Client Credentials Grant 방식을 사용하며,
     * Keycloak Admin REST API 호출 시 Authorization 헤더에 사용된다.</p>
     *
     * @return 관리자 권한 Access Token
     * @throws CustomApiException 토큰 발급 실패 또는 응답이 비정상적인 경우
     */
    public String getAdminAccessToken() {
        String tokenUrl = baseUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);

        // Keycloak 토큰 응답: access_token, expires_in, token_type ...
        TokenResponse resp = restClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(form)
                .retrieve()
                .onStatus(s -> s.is4xxClientError() || s.is5xxServerError(), (req, res) -> {
                    String body = new String(res.getBody().readAllBytes());
                    throw new CustomApiException(INTERNAL_SERVER_ERROR.value(),ILLEGALSTATE,"Keycloak token request failed. status="
                            + res.getStatusCode() + ", body=" + body);
                })
                .body(TokenResponse.class);

        if (resp == null || resp.accessToken() == null || resp.accessToken().isBlank()) {
            throw new CustomApiException(INTERNAL_SERVER_ERROR.value(),ILLEGALSTATE,"Failed to get Keycloak admin access token.");
        }

        return resp.accessToken();
    }

    /**
     * 지정한 Keycloak 사용자를 비활성화한다(enabled=false).
     *
     * <p>Keycloak Admin API를 호출하여 사용자 계정을 비활성화하며,
     * Keycloak 장애 발생 시 Circuit Breaker에 의해 보호된다.</p>
     *
     * @param keycloakUserId 비활성화할 Keycloak 사용자 ID
     */
    @CircuitBreaker(name="keycloakUserDisable", fallbackMethod="disableUserFallback")
    public void disableUser(String keycloakUserId) {
        String adminToken = getAdminAccessToken();

        String url = baseUrl + "/admin/realms/" + realm + "/users/" + keycloakUserId;

        restClient.put()
                .uri(url)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(APPLICATION_JSON)
                .body(new UserUpdateRequest(false)) // enabled=false
                .retrieve()
                .toBodilessEntity();
    }

    /**
     * 사용자 비활성화 요청에 대한 Circuit Breaker 폴백 메서드.
     *
     * <p>비활성화 작업은 대체 성공 경로가 없으므로,
     * 즉시 예외를 발생시켜 상위 계층에서 장애로 처리한다.</p>
     *
     * @param keycloakUserId 비활성화 요청 대상 사용자 ID
     * @param t Circuit Breaker에 의해 전달된 예외
     */
    public void disableUserFallback(String keycloakUserId, Throwable t) {
        throw new RuntimeException("Keycloak disable failed for userId=" + keycloakUserId, t);
    }

}
