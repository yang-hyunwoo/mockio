package com.mockio.auth_service.controller;

import com.mockio.auth_service.dto.AuthSession;
import com.mockio.auth_service.util.AuthSessionStore;
import com.mockio.auth_service.util.PkceStore;
import com.mockio.auth_service.util.PkceUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth/v1/public")
@RequiredArgsConstructor
public class AuthLoginController {

    private final RestClient restClient = RestClient.create();

    private final PkceStore pkceStore;

    private final AuthSessionStore authSessionStore;

    @Value("${keycloak.issuer}")
    private String issuer;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.redirect-uri}")
    private String redirectUri;

    @Value("${keycloak.scope}")
    private String scope;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @GetMapping("/login")
    public void login(HttpServletResponse response) {
        String state = PkceUtil.generateState();
        String codeVerifier = PkceUtil.generateCodeVerifier();
        String codeChallenge = PkceUtil.toCodeChallengeS256(codeVerifier);

        // state -> verifier 저장 (TTL 5분)
        pkceStore.saveVerifier(state, codeVerifier);

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

    @GetMapping("/callback")
    public void callback(@RequestParam String code,
                         @RequestParam String state,
                         HttpServletResponse response,
                         HttpServletRequest request) throws Exception {

        String codeVerifier = pkceStore.consumeVerifier(state);

        if (codeVerifier == null) {
            response.sendError(400, "Invalid state or expired login attempt.");
            return;
        }

        String tokenEndpoint = issuer + "/protocol/openid-connect/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", clientId);
        form.add("code", code);
        form.add("redirect_uri", redirectUri);
        form.add("code_verifier", codeVerifier);

        if (clientSecret != null && !clientSecret.isBlank()) {
            form.add("client_secret", clientSecret);
        }

        Map tokenResponse = restClient.post()
                .uri(tokenEndpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(Map.class);

        String accessToken = (String) tokenResponse.get("access_token");
        String refreshToken = (String) tokenResponse.get("refresh_token");
        Integer expiresIn = (Integer) tokenResponse.get("expires_in");

        if (refreshToken == null || accessToken == null || expiresIn == null) {
            response.sendError(500, "Token response missing fields.");
            return;
        }

        String sessionId = UUID.randomUUID().toString();
        long accessExpiresAt = Instant.now().plusSeconds(expiresIn).getEpochSecond();

        //  Redis 저장 (TTL은 정책으로: 예 7일 또는 refresh 만료에 맞춰)
        AuthSession session = new AuthSession(refreshToken, accessToken, accessExpiresAt);
        authSessionStore.save(sessionId, session, Duration.ofDays(7));

        //  브라우저에 세션 쿠키 (HttpOnly)
        ResponseCookie cookie = ResponseCookie.from("MOCKIO_SESSION", sessionId)
                .httpOnly(true)
                .secure(false)      // 로컬 http면 false, 운영 https면 true
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 4) 프론트로 redirect
        response.sendRedirect("http://localhost:3000/");
    }

}
