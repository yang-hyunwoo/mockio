package com.mockio.auth_service.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwkConfig {

    @Value("${jwt.public-key}")
    private String publicKeyPem;

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

    private RSAPublicKey getPublicKey() {
        try {
            String key = publicKeyPem
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
