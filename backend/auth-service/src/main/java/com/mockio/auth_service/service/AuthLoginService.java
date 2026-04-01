package com.mockio.auth_service.service;

/**
 * 인증 관련 비즈니스 로직을 처리하는 서비스 클래스
 *
 * 로그인, 토큰 재발급, 사용자 조회, 로그아웃 기능을 제공한다.
 * Spring Security 인증(AuthenticationManager)과 JWT 기반 토큰 발급,
 * Redis를 통한 Refresh Token 관리, 외부 user-service 연동을 담당한다.
 */

import com.mockio.auth_service.client.UserClient;
import com.mockio.auth_service.config.JwtTokenProvider;
import com.mockio.auth_service.dto.LoginUser;
import com.mockio.auth_service.dto.response.UserInfoResponse;
import com.mockio.auth_service.dto.request.UserLoginRequest;
import com.mockio.auth_service.dto.response.LoginResponse;
import com.mockio.common_core.constant.CommonErrorEnum;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.common_spring.util.CustomCookie;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthLoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserClient userClient;
    private final RedisRefreshTokenService redisRefreshTokenService;
    private final CustomCookie customCookie;
    private static final String refreshCookieName = "refreshToken";

    /**
     * 로그인 처리
     *
     * 사용자 인증을 수행하고, 성공 시 Access Token과 Refresh Token을 발급한다.
     * Refresh Token은 쿠키 및 Redis에 저장되며,
     * 인증 실패 시 로그인 실패 횟수를 증가시킨다.
     */
    public LoginResponse login(UserLoginRequest request , HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        try {
            Authentication authentication = authenticationManager.authenticate(authToken);
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();

            userClient.resetFailCount(loginUser.getUserId());
            String accessToken = jwtTokenProvider.createAccessToken(loginUser.getUserId());
            String refreshToken = jwtTokenProvider.createRefreshToken(loginUser);

            ResponseCookie refreshTokenCookie = customCookie.createCookie(refreshCookieName, refreshToken, (1 * 24 * 60 * 60));

            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
            redisRefreshTokenService.save(
                    loginUser.getUserId(),
                    refreshToken,
                    Duration.ofDays(1)
            );

            return new LoginResponse(
                    loginUser.getUserId(),
                    loginUser.getEmail(),
                    accessToken
            );
        } catch (BadCredentialsException e) {
            userClient.loginFailure(request.email());
            throw e;
        }
    }

    /**
     * 토큰 재발급 처리
     *
     * 요청 쿠키에서 Refresh Token을 추출하여 검증 후,
     * 새로운 Access Token을 발급한다.
     * Redis에 저장된 Refresh Token과 일치 여부를 확인한다.
     */
    public LoginResponse refresh(HttpServletRequest request){
        String refreshToken = extractRefreshToken(request);

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new CustomApiException(401, CommonErrorEnum.ERR_000, CommonErrorEnum.ERR_000.getMessage());
        }

        // 1. refreshToken 검증
        jwtTokenProvider.validateToken(refreshToken);

        // 2. 사용자 정보 추출
        Long userId = jwtTokenProvider.getUserId(refreshToken);

        String savedRefreshToken = redisRefreshTokenService.get(userId);

        if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
            throw new CustomApiException(401, CommonErrorEnum.ERR_000, CommonErrorEnum.ERR_000.getMessage());
        }

        // 3. accessToken 재발급
        String newAccessToken = jwtTokenProvider.createAccessToken(userId);

        return new LoginResponse(
                null,
                null,
                newAccessToken
        );
    }

    /**
     * 사용자 상세 조회
     *
     * user-service를 호출하여 사용자 정보를 조회한다.
     */
    public UserInfoResponse userDetail(Long userId) {
        return userClient.userDetail(userId);
    }

    /**
     * 로그아웃 처리
     *
     * Refresh Token을 Redis에서 삭제하고,
     * 클라이언트 쿠키에서 Refresh Token을 제거한다.
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshToken(request);

        if (refreshToken == null || refreshToken.isBlank()) {
            ResponseCookie refreshToken1 = customCookie.deleteCookie(refreshCookieName);
            response.addHeader(HttpHeaders.SET_COOKIE, refreshToken1.toString());
        }
        Long userId = jwtTokenProvider.ignoreTokenValid(refreshToken);

        // 2. Redis 삭제
        redisRefreshTokenService.delete(userId);

        // 3. 쿠키 제거
        ResponseCookie refreshToken2 = customCookie.deleteCookie(refreshCookieName);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshToken2.toString());
    }

    /**
     * Refresh Token 추출
     *
     * HttpServletRequest의 쿠키에서 "refreshToken" 값을 찾아 반환한다.
     * 존재하지 않을 경우 null을 반환한다.
     */
    private String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (refreshCookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
