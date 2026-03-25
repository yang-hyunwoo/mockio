package com.mockio.auth_service.oauth;

import com.mockio.auth_service.config.EnvironmentProvider;
import com.mockio.auth_service.config.JwtTokenProvider;
import com.mockio.auth_service.dto.LoginUser;
import com.mockio.auth_service.service.RedisRefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;



@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisRefreshTokenService redisRefreshTokenService;

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
                Duration.ofDays(1)
        );

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false); // 운영 HTTPS면 true
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(1 * 24 * 60 * 60);
        response.addCookie(refreshCookie);

        String targetUrl = determineTargetUrl();

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrl() {
        if (EnvironmentProvider.isProd()) {
            return "https://your-domain.com/social/callback";
        }
        return "http://localhost:3000/social/callback";
    }



}
