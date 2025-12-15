package com.mockio.user_service.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 목적:
 * - /api/user/public/** 은 permitAll (토큰 없어도 200)
 * - 나머지는 authenticated (토큰 없으면 EntryPoint 동작: 현재 구현 기준 403 + Response JSON)
 * - 토큰 있으면 200
 *
 * 주의:
 * - 현재 AccessDeniedHandler(인가 실패) 케이스는 SecurityConfig에 role 규칙이 없어서 재현 불가
 */
@SpringBootTest(properties = {
        "spring.security.oauth2.resourceserver.jwt.issuer-uri="
})
@AutoConfigureMockMvc
@Import(SecurityConfigTest.TestApi.class)
class SecurityConfigTest {

    @Autowired
    MockMvc mockMvc;

    /**
     * Resource Server 설정이 JwtDecoder 빈을 요구하므로,
     * 테스트에서 외부 issuer-uri 접근을 막기 위해 이름까지 지정해 오버라이드합니다.
     */
    @MockBean
    JwtDecoder jwtDecoder;

    @Test
    @DisplayName("PUBLIC 경로는 토큰 없이 접근 가능하다 (200 OK)")
    void public_endpoint_allows_access_without_token() throws Exception {
        mockMvc.perform(get("/api/user/public/ping"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("public-pong"));
    }

    @Test
    @DisplayName("보호된 경로는 토큰이 없으면 EntryPoint가 동작하고 403 에러를 반환한다")
    void protected_endpoint_returns_403_and_invokes_entrypoint_when_token_missing()  throws Exception {
        mockMvc.perform(get("/api/user/private/ping"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // Response 포맷 검증
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.httpCode").value(403))
                .andExpect(jsonPath("$.message", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.errCode", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.errCodeMsg", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    @DisplayName("보호된 경로는 유효한 JWT가 있으면 접근 가능하다 (200 OK)")
    void protected_endpoint_allows_access_with_valid_jwt() throws Exception {
        mockMvc.perform(get("/api/user/private/ping").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("private-pong"));
    }

    @TestConfiguration
    static class TestApi {

        @RestController
        static class PingController {

            @GetMapping(value = "/api/user/public/ping", produces = MediaType.TEXT_PLAIN_VALUE)
            public String publicPing() {
                return "public-pong";
            }

            @GetMapping(value = "/api/user/private/ping", produces = MediaType.TEXT_PLAIN_VALUE)
            public String privatePing() {
                return "private-pong";
            }
        }
    }
}
