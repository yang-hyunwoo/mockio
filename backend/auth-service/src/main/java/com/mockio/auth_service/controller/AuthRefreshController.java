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

import com.mockio.auth_service.dto.AuthSession;
import com.mockio.auth_service.dto.response.KeycloakTokenResponse;
import com.mockio.auth_service.dto.response.RefreshResponse;
import com.mockio.auth_service.dto.response.SessionValidateResponse;
import com.mockio.auth_service.service.RefreshService;
import com.mockio.auth_service.util.AuthSessionStore;
import com.mockio.auth_service.util.CookieFactory;
import com.mockio.auth_service.util.JwtClaimUtil;
import com.mockio.common_core.constant.CommonErrorEnum;
import com.mockio.common_core.exception.RefreshTokenMissingException;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.common_spring.util.response.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/v1")
@Slf4j
public class AuthRefreshController {

    private final RefreshService refreshService;
    private final CookieFactory cookieFactory;
    private final AuthSessionStore authSessionStore;
    private final JwtClaimUtil jwtClaimUtil;
    private final MessageUtil messageUtil;

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
    public ResponseEntity<Response<SessionValidateResponse>> refresh(@CookieValue(name = "MOCKIO_SESSION", required = false) String sessionId) {

        AuthSession s = authSessionStore.find(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid session"));


        s = refreshService.refreshBy(sessionId, s);
        return Response.ok(messageUtil.getMessage("response.read"),
                jwtClaimUtil.verifyAndExtract(s.accessToken()));
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
