package com.mockio.auth_service.controller;

/**
 * Access Token 재발급(refresh)을 담당하는 인증 컨트롤러.
 *
 * <p>Refresh Token은 HttpOnly 쿠키에서만 추출하며,
 * 클라이언트는 Access Token만 응답 바디로 전달받아
 * 메모리에 보관 후 Authorization 헤더로 사용한다.</p>
 *
 * <p>Refresh Token 회전(rotation)이 발생한 경우,
 * 새 Refresh Token을 쿠키로 다시 설정한다.</p>
 */

import com.mockio.auth_service.dto.response.KeycloakTokenResponse;
import com.mockio.auth_service.dto.response.RefreshResponse;
import com.mockio.auth_service.service.RefreshService;
import com.mockio.auth_service.util.CookieFactory;
import com.mockio.common_core.constant.CommonErrorEnum;
import com.mockio.common_core.exception.RefreshTokenMissingException;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/v1")
@Slf4j
public class AuthRefreshController {

    private final RefreshService refreshService;
    private final CookieFactory cookieFactory;

    /**
     * Refresh Token을 이용해 새로운 Access Token을 발급한다.
     *
     * <p>처리 흐름:
     * <ol>
     *   <li>요청 쿠키에서 Refresh Token 추출</li>
     *   <li>Refresh Token 유효성 검증 및 Keycloak 토큰 갱신</li>
     *   <li>필요 시 Refresh Token 쿠키 재설정</li>
     *   <li>새 Access Token을 응답 바디로 반환</li>
     * </ol>
     *
     * @param request HTTP 요청 객체 (Refresh Token 쿠키 포함)
     * @return 새 Access Token 정보가 포함된 응답
     * @throws RefreshTokenMissingException Refresh Token 쿠키가 존재하지 않는 경우
     */
    @PostMapping("/refresh")
    public ResponseEntity<Response<RefreshResponse>> refresh(HttpServletRequest request) {
        String refreshToken = extractCookie(request, cookieFactory.refreshCookieName());

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new RefreshTokenMissingException(CommonErrorEnum.ERR_REFRESH_TOKEN_MISSING.getMessage());
        }

        KeycloakTokenResponse kc = refreshService.refreshBy(refreshToken);

        // Keycloak이 refresh_token을 다시 내려주는 경우(회전/갱신) 쿠키 갱신
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

    /**
     * 요청 쿠키에서 지정된 이름의 쿠키 값을 추출한다.
     *
     * @param request HTTP 요청
     * @param name 추출할 쿠키 이름
     * @return 쿠키 값, 없으면 null
     */
    private String extractCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        for (var c : request.getCookies()) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }

}
