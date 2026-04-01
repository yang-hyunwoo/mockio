package com.mockio.auth_service.oauth;

/**
 * 소셜 로그인 성공 Handler
 *
 * 로그인 성공 시 refreshToken 생성후 쿠키로 내려 준다.
 * 그후 callback 페이지를 호출 한다.
 *
 */

import com.mockio.auth_service.config.JwtTokenProvider;
import com.mockio.auth_service.dto.LoginUser;
import com.mockio.auth_service.service.RedisRefreshTokenService;
import com.mockio.common_spring.util.CustomCookie;
import com.mockio.common_spring.util.EnvironmentProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisRefreshTokenService redisRefreshTokenService;

    private final EnvironmentProvider environmentProvider;

    private final CustomCookie customCookie;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String refreshToken = jwtTokenProvider.createRefreshToken(loginUser);
        log.info("authentication = {}", authentication);
        log.info("principal class = {}", authentication.getPrincipal().getClass());
        log.info("authentication name = {}", authentication.getName());

        redisRefreshTokenService.save(
                loginUser.getUserId(),
                refreshToken,
                Duration.ofDays(3)
        );

        ResponseCookie refreshTokenCookie = customCookie.createCookie("refreshToken", refreshToken, (1 * 24 * 60 * 60));
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        String targetUrl = determineTargetUrl();

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrl() {
        if (environmentProvider.isProd()) {
            return "https://mockio.cloud/social/callback";
        }
        return "http://localhost:3000/social/callback";
    }

}
