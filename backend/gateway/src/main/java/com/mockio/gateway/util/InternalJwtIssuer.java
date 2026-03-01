package com.mockio.gateway.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InternalJwtIssuer {

    private final RSAKey rsaKey;

    public String issue(String userId, List<String> roles) {

        RSAPrivateKey privateKey = null;
        try {
            privateKey = rsaKey.toRSAPrivateKey();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(userId)
                .claim("roles", roles)
                .setIssuer("mockio-gateway")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(600)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .setHeaderParam("kid", rsaKey.getKeyID())
                .compact();
    }
}