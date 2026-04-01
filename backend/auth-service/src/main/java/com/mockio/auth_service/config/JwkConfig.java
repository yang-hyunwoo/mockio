package com.mockio.auth_service.config;

/**
 * JWT 검증을 위한 JWKSet Bean 생성 설정 클래스
 *
 * - 공개키(RSA Public Key)를 로드하여 JWK(JSON Web Key) 형태로 변환
 * - Resource Server에서 사용할 JWKSet을 Spring Bean으로 등록
 *
 * 환경별 동작:
 * - 운영(prod): 외부 경로(publicKeyPath)에서 파일 읽기
 * - 개발/로컬: classpath에서 키 파일 로드
 */

import com.mockio.common_spring.util.EnvironmentProvider;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class JwkConfig {

    @Value("${jwt.public-key-path}")
    private String publicKeyPath;

    private final EnvironmentProvider environmentProvider;

    /**
     * JWKSet Bean 생성
     * - RSA 공개키를 기반으로 RSAKey 생성
     * - keyID를 포함한 JWK 구성
     * - 최종적으로 JWKSet으로 래핑하여 반환
     * @return JWKSet (JWT 검증용 공개키 집합)
     */
    @Bean
    public JWKSet jwkSet() {
        try {
            RSAPublicKey publicKey = getPublicKey();

            RSAKey rsaKey = new RSAKey.Builder(publicKey)
                    .keyID("mockio-auth-key")
                    .build();

            return new JWKSet(rsaKey);
        } catch (Exception e) {
            throw new RuntimeException("JWKSet 생성 실패", e);
        }
    }

    /**
     * 공개키(RSA Public Key) 로드 및 변환
     * 처리 과정:
     * 1. 환경에 따라 파일 경로 또는 classpath에서 키 문자열 로드
     * 2. PEM 헤더/푸터 제거
     * 3. Base64 디코딩
     * 4. X509EncodedKeySpec으로 변환
     * 5. RSA PublicKey 객체 생성
     * @return RSAPublicKey (JWT 서명 검증용)
     */
    private RSAPublicKey getPublicKey() {
        try {
            String publicKeyString;
            if(environmentProvider.isProd()) {
                publicKeyString = Files.readString(Paths.get(publicKeyPath));
            } else {
                Resource resource = new ClassPathResource(publicKeyPath);
                publicKeyString= new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            }

            String key = publicKeyString
                    .replace("-----BEGIN RSA PUBLIC KEY-----", "")
                    .replace("-----END RSA PUBLIC KEY-----", "")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Public key 로딩 실패", e);
        }
    }

}
