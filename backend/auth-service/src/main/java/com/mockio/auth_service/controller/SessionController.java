package com.mockio.auth_service.controller;

import com.mockio.auth_service.dto.AuthSession;
import com.mockio.auth_service.dto.response.SessionValidateResponse;
import com.mockio.auth_service.util.AuthSessionStore;
import com.mockio.auth_service.util.JwtClaimUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/v1/public/session")
@RequiredArgsConstructor
public class SessionController {

    private final AuthSessionStore authSessionStore;

    @GetMapping("/validate")
    public SessionValidateResponse validate(@CookieValue(name = "MOCKIO_SESSION", required = false) String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing session");
        }

        AuthSession s = authSessionStore.find(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid session"));

        return JwtClaimUtil.parseSub(s.accessToken());
    }

}
