package com.mockio.auth_service.config;


import com.mockio.auth_service.controller.AuthRefreshController;
import com.mockio.auth_service.dto.response.KeycloakTokenResponse;
import com.mockio.auth_service.service.RefreshService;
import com.mockio.auth_service.util.CookieFactory;
import com.mockio.common_core.constant.CommonErrorEnum;
import com.mockio.common_core.exception.RefreshTokenInvalidException;
import com.mockio.common_spring.util.MessageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Coverage:
 * 1) /api/auth/v1/refresh : permitAll + controller-level cookie validation
 *    - missing cookie -> 401 + error JSON
 *    - invalid refresh -> 401 + error JSON
 *    - success -> 200 + success JSON (+ Set-Cookie when rotation present)
 *
 * 2) /api/auth/v1/naver/userinfo : IP allowlist
 *    - allowed IP -> 200
 *    - not allowed -> 403
 *
 * 3) denyAll chain
 *    - any other path -> 403
 */
@WebMvcTest(controllers = {
        TestEndpointsController.class,
        AuthRefreshController.class
})
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired MockMvc mockMvc;

    @MockBean CookieFactory cookieFactory;

    @MockBean RefreshService refreshService;

    @MockBean MessageUtil messageUtil;

    @Autowired
    org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        Mockito.when(cookieFactory.refreshCookieName()).thenReturn("refresh_token");
        Mockito.when(cookieFactory.refreshCookie(anyString(), anyLong()))
                .thenAnswer(inv -> {
                    String token = inv.getArgument(0, String.class);
                    long maxAge = inv.getArgument(1, Long.class); // anyLong()이면 Boxing되어 Long으로 들어옵니다.

                    return ResponseCookie.from("refresh_token", token)
                            .httpOnly(true)
                            .path("/")
                            .maxAge(maxAge)
                            .build();
                });
    }

    @RestController
    static class TestEndpoints {
        @GetMapping(value = "/api/auth/v1/naver/userinfo", produces = MediaType.TEXT_PLAIN_VALUE)
        public String userinfo() {
            return "userinfo-ok";
        }

        @GetMapping(value = "/api/auth/v1/other", produces = MediaType.TEXT_PLAIN_VALUE)
        public String other() {
            return "other-ok";
        }
    }

    @Test
    @DisplayName("리프레시 쿠키가 없으면 /refresh 는 401 에러를 반환한다")
    void refresh_returns_401_when_cookie_missing() throws Exception {
        mockMvc.perform(post("/api/auth/v1/refresh"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.httpCode").value(401))
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.errCode").value(CommonErrorEnum.ERR_REFRESH_TOKEN_MISSING.getCode()))
                .andExpect(jsonPath("$.errCodeMsg", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.timestamp", notNullValue()));

        Mockito.verifyNoInteractions(refreshService);
    }

    @Test
    @DisplayName("리프레시 토큰이 유효하지 않으면 /refresh 는 401 에러를 반환한다")
    void refresh_returns_401_when_refresh_invalid() throws Exception {
        String cookieName = cookieFactory.refreshCookieName();

        Mockito.when(refreshService.refreshBy(anyString()))
                .thenThrow(new RefreshTokenInvalidException("invalid_grant", null));

        mockMvc.perform(post("/api/auth/v1/refresh")
                        .cookie(new MockCookie(cookieName, "bad-refresh-token")))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.httpCode").value(401))
                .andExpect(jsonPath("$.message").doesNotExist())
                // 핸들러에서 ERR_REFRESH_TOKEN_INVALID로 매핑한다고 하셨으니 그 코드로 검증
                .andExpect(jsonPath("$.errCode").value(CommonErrorEnum.ERR_REFRESH_TOKEN_INVALID.getCode()))
                .andExpect(jsonPath("$.errCodeMsg", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.timestamp", notNullValue()));

        Mockito.verify(refreshService, Mockito.times(1)).refreshBy(anyString());
    }

    @Test
    @DisplayName("리프레시 성공 시 /refresh 는 200과 새 액세스 토큰을 반환한다")
    void refresh_returns_200_when_success() throws Exception {
        String cookieName = cookieFactory.refreshCookieName();

        // refresh token rotation 없음(쿠키 갱신 헤더도 없음)
        KeycloakTokenResponse kc = new KeycloakTokenResponse(
                "new-access-token",
                null,
                "Bearer",
                300L,
                null
        );

        Mockito.when(refreshService.refreshBy(anyString()))
                .thenReturn(kc);

        mockMvc.perform(post("/api/auth/v1/refresh")
                        .cookie(new MockCookie(cookieName, "good-refresh-token")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.httpCode").value(200))
                .andExpect(jsonPath("$.message").value("ACCESS_TOKEN_REFRESHED"))
                .andExpect(jsonPath("$.data.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.data.expiresIn").value(300))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                // rotation이 없으므로 Set-Cookie 없어야 함
                .andExpect(header().doesNotExist(HttpHeaders.SET_COOKIE));

        Mockito.verify(refreshService, Mockito.times(1)).refreshBy(anyString());
    }

    @Test
    @DisplayName("리프레시 토큰 회전이 발생하면 /refresh 는 Set-Cookie 헤더를 내려준다")
    void refresh_sets_cookie_when_rotation_present() throws Exception {
        String cookieName = cookieFactory.refreshCookieName();

        // rotation 있음 -> controller가 Set-Cookie 내려야 함
        KeycloakTokenResponse kc = new KeycloakTokenResponse(
                "new-access-token",
                "rotated-refresh-token",
                "Bearer",
                300L,
                3600L
        );

        Mockito.when(refreshService.refreshBy(anyString()))
                .thenReturn(kc);

        mockMvc.perform(post("/api/auth/v1/refresh")
                        .cookie(new MockCookie(cookieName, "good-refresh-token")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("ACCESS_TOKEN_REFRESHED"))
                .andExpect(jsonPath("$.data.accessToken").value("new-access-token"))
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString(cookieName + "=rotated-refresh-token")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("HttpOnly")));

        Mockito.verify(refreshService, Mockito.times(1)).refreshBy(anyString());
    }

    @Test
    @DisplayName("허용된 IP에서 호출하면 네이버 유저정보 프록시는 200을 반환한다")
    void userinfo_returns_200_when_ip_allowed() throws Exception {
        // SecurityConfig에서 KEYCLOAK_IP = 127.0.0.1
        mockMvc.perform(get("/api/auth/v1/naver/userinfo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer dummy") // 필수 헤더
                        .with(req -> {
                            req.setRemoteAddr("127.0.0.1");
                            return req;
                        }))

                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("허용되지 않은 IP에서 호출하면 네이버 유저정보 프록시는 403을 반환한다")
    void userinfo_returns_403_when_ip_not_allowed() throws Exception {
        mockMvc.perform(get("/api/auth/v1/naver/userinfo")
                        .with(req -> { req.setRemoteAddr("10.10.10.10"); return req; }))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("명시되지 않은 인증 API는 기본적으로 접근이 차단된다")
    void other_endpoints_are_denied_by_default() throws Exception {
        mockMvc.perform(get("/api/auth/v1/other"))
                .andExpect(status().isForbidden());
    }
}
