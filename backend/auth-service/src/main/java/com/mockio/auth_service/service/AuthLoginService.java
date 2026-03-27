package com.mockio.auth_service.service;

import com.mockio.auth_service.client.UserProfileClient;
import com.mockio.auth_service.config.EnvironmentProvider;
import com.mockio.auth_service.config.JwtTokenProvider;
import com.mockio.auth_service.dto.LoginUser;
import com.mockio.auth_service.dto.UserInfoResponse;
import com.mockio.auth_service.dto.request.UserLoginRequest;
import com.mockio.auth_service.dto.response.LoginResponse;
import com.mockio.common_core.constant.CommonErrorEnum;
import com.mockio.common_core.exception.CustomApiException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
    private final UserProfileClient userProfileClient;
    private final RedisRefreshTokenService redisRefreshTokenService;

    public LoginResponse login(UserLoginRequest request , HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        try {
            Authentication authentication = authenticationManager.authenticate(authToken);
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            // 성공
            userProfileClient.resetFailCount(loginUser.getUserId());
            String accessToken = jwtTokenProvider.createAccessToken(loginUser.getUserId());
            String refreshToken = jwtTokenProvider.createRefreshToken(loginUser);

            Cookie refreshCookie = new Cookie("refreshToken", refreshToken);

            redisRefreshTokenService.save(
                    loginUser.getUserId(),
                    refreshToken,
                    Duration.ofDays(1)
            );

            refreshCookie.setHttpOnly(true);
            if (EnvironmentProvider.isProd()) {
                refreshCookie.setSecure(true);
            } else {
                refreshCookie.setSecure(false);
            }
            // 운영 HTTPS면 true
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(1 * 24 * 60 * 60);

            response.addCookie(refreshCookie);

            return new LoginResponse(
                    loginUser.getUserId(),
                    loginUser.getEmail(),
                    accessToken
            );
        } catch (BadCredentialsException e) {
            userProfileClient.loginFailure(request.email());
            throw e;
        }
    }

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

    public UserInfoResponse userDetail(Long userId) {
        return userProfileClient.userDetail(userId);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshToken(request);

        if (refreshToken == null || refreshToken.isBlank()) {
            expireRefreshCookie(response);
        }

        try {
            // 1. 토큰에서 userId 추출 만료되어도 가져옴
            Long userId = jwtTokenProvider.ignoreTokenValid(refreshToken);

            // 2. Redis 삭제
            redisRefreshTokenService.delete(userId);

        } catch (Exception e) {
        }

        // 3. 쿠키 제거
        expireRefreshCookie(response);
    }

    private String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void expireRefreshCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        if (EnvironmentProvider.isProd()) {
            cookie.setSecure(true);
        } else {
            cookie.setSecure(false);
        }
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
