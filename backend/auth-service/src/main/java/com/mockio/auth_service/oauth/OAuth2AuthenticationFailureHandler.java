package com.mockio.auth_service.oauth;

import com.mockio.common_spring.util.EnvironmentProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final EnvironmentProvider environmentProvider;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception
    ) throws IOException {
        String url = "http://localhost:3000";
        if(environmentProvider.isProd()) {
            url = "https://mockio.cloud";
        }

        String targetUrl = UriComponentsBuilder.fromUriString(url)
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}
