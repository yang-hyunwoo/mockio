package com.mockio.auth_service.controller;

import com.mockio.auth_service.util.PkceUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/auth/v1/public")
public class AuthLoginController {

    @Value("${keycloak.issuer}")
    private String issuer;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.redirect-uri}")
    private String redirectUri;

    @Value("${keycloak.scope}")
    private String scope;

//    @Value("${keycloak.post-login-redirect:}")
//    private String postLoginRedirect;
//
//    @Value("${auth.service-base-url}")
//    private String authServiceBaseUrl;

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws Exception {
        String state = PkceUtil.generateState();
        String codeVerifier = PkceUtil.generateCodeVerifier();
        String codeChallenge = PkceUtil.toCodeChallengeS256(codeVerifier);

        // state -> verifier 저장 (TTL 5분)
        String key = "pkce:" + state;
//        redis.opsForValue().set(key, codeVerifier, Duration.ofMinutes(5));

        String authorizationEndpoint = issuer + "/protocol/openid-connect/auth";

        URI redirect = UriComponentsBuilder
                .fromHttpUrl(authorizationEndpoint)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", scope)
                .queryParam("state", state)
                .queryParam("code_challenge", codeChallenge)
                .queryParam("code_challenge_method", "S256")
                .build()
                .encode()
                .toUri();

        response.setStatus(302);
        response.setHeader("Location", redirect.toString());
    }
}
