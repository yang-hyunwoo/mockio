package com.mockio.gateway.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@SpringBootTest
@AutoConfigureWebTestClient
@Import(SecurityConfigTest.TestRoutes.class)
class SecurityConfigTest {

    @Autowired
    WebTestClient webTestClient;

    /**
     * SecurityConfig의 @Bean jwtDecoder()를 "이 이름"으로 오버라이드해서
     * fromIssuerLocation() 외부 호출이 발생하지 않도록 막는다.
     */
    @MockBean(name = "jwtDecoder")
    JwtDecoder jwtDecoder;

    @Test
    @DisplayName("Users public endpoint allows access without token (200 OK)")
    void users_public_endpoint_allows_access_without_token()  {
        webTestClient.get()
                .uri("/api/users/v1/public/ping")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_PLAIN)
                .expectBody(String.class).isEqualTo("users-public-pong");
    }

    @Test
    @DisplayName("Auth public endpoint allows access without token (200 OK)")
    void auth_public_endpoint_allows_access_without_token()  {
        webTestClient.get()
                .uri("/api/auth/v1/public/ping")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_PLAIN)
                .expectBody(String.class).isEqualTo("auth-public-pong");
    }

    @Test
    @DisplayName("Protected endpoint returns 401 and JSON error response when token is missing")
    void protected_endpoint_returns_401_and_json_response_when_token_missing() {
        webTestClient.get()
                .uri("/api/users/v1/private/ping")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.resultCode").isEqualTo("ERROR")
                .jsonPath("$.httpCode").isEqualTo(401)
                .jsonPath("$.message").exists()
                .jsonPath("$.errCode").exists()
                .jsonPath("$.errCodeMsg").exists()
                .jsonPath("$.timestamp").exists();
    }

    @Test
    @DisplayName("Protected endpoint allows access when valid JWT is present (200 OK)")
    void protected_endpoint_allows_access_with_valid_jwt()  {
        webTestClient.mutateWith(mockJwt())
                .get()
                .uri("/api/users/v1/private/ping")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_PLAIN)
                .expectBody(String.class).isEqualTo("users-private-pong");
    }

    @TestConfiguration
    static class TestRoutes {
        @Bean
        RouterFunction<ServerResponse> testRouter() {
            return RouterFunctions
                    .route(GET("/api/users/v1/public/ping"),
                            req -> ServerResponse.ok()
                                    .contentType(MediaType.TEXT_PLAIN)
                                    .bodyValue("users-public-pong"))
                    .andRoute(GET("/api/auth/v1/public/ping"),
                            req -> ServerResponse.ok()
                                    .contentType(MediaType.TEXT_PLAIN)
                                    .bodyValue("auth-public-pong"))
                    .andRoute(GET("/api/users/v1/private/ping"),
                            req -> ServerResponse.ok()
                                    .contentType(MediaType.TEXT_PLAIN)
                                    .bodyValue("users-private-pong"));
        }
    }
}
