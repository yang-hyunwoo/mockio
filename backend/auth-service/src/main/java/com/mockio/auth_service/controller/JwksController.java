package com.mockio.auth_service.controller;

/**
 * JWKS(JSON Web Key Set) 제공 컨트롤러
 *
 * JWT 서명 검증에 필요한 공개키(public key)를 외부 서비스에 제공한다.
 * OAuth2 Resource Server 또는 다른 마이크로서비스에서
 * 해당 엔드포인트를 통해 공개키를 조회하여 토큰의 서명을 검증할 수 있다.
 */

import com.nimbusds.jose.jwk.JWKSet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "인증",
        description = """
                JWT 서명 검증에 필요한 공개키(public key)를 외부 서비스에 제공 API입니다.
                
                """
)
@RestController
@RequiredArgsConstructor
public class JwksController {

    private final JWKSet jwkSet;

    /**
     * JWKS 조회 API
     *
     * 현재 서버가 보유한 공개키 집합(JWKSet)을 JSON 형태로 반환한다.
     * 클라이언트 또는 외부 서비스는 이 키를 사용하여 JWT 서명을 검증한다.
     */
    @Operation(summary = "JWKS 조회")
    @GetMapping("/api/auth/v1/.well-known/jwks.json")
    public Map<String, Object> keys() {
        return jwkSet.toJSONObject();
    }

}
