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

import com.mockio.common_core.constant.CommonErrorEnum;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.common_spring.util.EnvironmentProvider;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static com.mockio.common_core.constant.CommonErrorEnum.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JwkConfig {

    @Value("${jwt.public-key-path}")
    private String publicKeyPath;

    @Value("${jwt.private-key-path}")
    private String privateKeyPath;

    private final EnvironmentProvider environmentProvider;

    @Bean
    public RSAPublicKey publicKey() {
        try {
            String publicKeyString;
            if (environmentProvider.isProd()) {
                publicKeyString = Files.readString(Paths.get(publicKeyPath));
            } else {
                Resource resource = new ClassPathResource(publicKeyPath);
                publicKeyString = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
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
            log.error("Private key 로딩 실패 : ", e);
            throw new CustomApiException(ERR_500.getHttpStatus(), ERR_500, "Public key 로딩 실패");
        }
    }

    /**
     * RSA PrivateKey 로드
     * 처리 과정:
     * 1. PEM 문자열 로드
     * 2. 헤더/푸터 제거
     * 3. Base64 디코딩
     * 4. PKCS8EncodedKeySpec 변환
     * 5. PrivateKey 객체 생성
     * @return PrivateKey (JWT 서명용)
     */
    @Bean
    public PrivateKey privateKey() {
        try {
            String privateKeyString;
            if (environmentProvider.isProd()) {
                privateKeyString = Files.readString(Paths.get(privateKeyPath));
            } else {
                Resource resource = new ClassPathResource(privateKeyPath);
                privateKeyString = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            }

            String key = privateKeyString
                    .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replace("-----END RSA PRIVATE KEY-----", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("Private key 로딩 실패 : ", e);
            throw new CustomApiException(ERR_500.getHttpStatus(), ERR_500, "Private key 로딩 실패");
        }
    }

    @Bean
    public JWKSet jwkSet(RSAPublicKey publicKey) {
        try {
            RSAKey rsaKey = new RSAKey.Builder(publicKey)
                    .keyID("mockio-auth-key")
                    .build();

            return new JWKSet(rsaKey);
        } catch (Exception e) {
            log.error("JWK Set 생성 실패 : ",e);
            throw new CustomApiException(ERR_500.getHttpStatus(), ERR_500, "JWKSet 생성 실패");
        }
    }

}
