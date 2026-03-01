package com.mockio.gateway.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtKeyConfig {

    @Bean
    public RSAKey rsaJwk() throws Exception {
        return new RSAKeyGenerator(2048)
                .keyID("mockio-key")
                .generate();
    }

    @Bean
    public JWKSet jwkSet(RSAKey rsaKey) {
        return new JWKSet(rsaKey.toPublicJWK());
    }
}
