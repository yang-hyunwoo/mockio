package com.mockio.auth_service.controller;

import com.nimbusds.jose.jwk.JWKSet;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JwksController {

    private final JWKSet jwkSet;

    @GetMapping("/api/auth/v1/.well-known/jwks.json")
    public Map<String, Object> keys() {
        return jwkSet.toJSONObject();
    }

}
