package com.mockio.auth_service.controller;

import com.mockio.auth_service.dto.KeycloakTokenResponse;
import com.mockio.auth_service.dto.RefreshResponse;
import com.mockio.auth_service.service.RefreshService;
import com.mockio.auth_service.util.CookieFactory;
import com.mockio.common_spring.constant.CommonErrorEnum;
import com.mockio.common_spring.exception.RefreshTokenMissingException;
import com.mockio.common_spring.util.response.Response;
import com.mockio.common_spring.util.response.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mockio.common_spring.constant.CommonErrorEnum.ERR_TOKEN_EXPIRED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/v1")
@Slf4j
public class AuthRefreshController {

    private final RefreshService refreshService;
    private final CookieFactory cookieFactory;

    @PostMapping("/refresh")
    public ResponseEntity<Response<RefreshResponse>> refresh(HttpServletRequest request) {
        String refreshToken = extractCookie(request, cookieFactory.refreshCookieName());

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new RefreshTokenMissingException(CommonErrorEnum.ERR_REFRESH_TOKEN_MISSING.getMessage());
        }

        KeycloakTokenResponse kc = refreshService.refreshBy(refreshToken);

        // (선택) Keycloak이 refresh_token을 다시 내려주는 경우(회전/갱신) 쿠키 갱신
        HttpHeaders headers = new HttpHeaders();
        if (kc.refreshToken() != null && !kc.refreshToken().isBlank() && kc.refreshExpiresIn() != null) {
            ResponseCookie cookie = cookieFactory.refreshCookie(kc.refreshToken(), kc.refreshExpiresIn());
            if (cookie != null) {
                headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            } else {
                log.error("Refresh cookie creation returned null");
            }

        }

        // Access는 바디로 반환(프론트가 메모리에 보관 후 Authorization 헤더로 사용)
        Response<RefreshResponse> body =
                ResponseBuilder.buildSuccess(
                        "ACCESS_TOKEN_REFRESHED",
                        new RefreshResponse(kc.accessToken(), kc.expiresIn())
                );
        return ResponseEntity.ok()
                .headers(headers)
                .body(body);
    }

    private String extractCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        for (var c : request.getCookies()) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }


}