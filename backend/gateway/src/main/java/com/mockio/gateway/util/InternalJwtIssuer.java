package com.mockio.gateway.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InternalJwtIssuer {

    private final KeyPair keyPair;

    public String issue(String userId, List<String> roles) {
        try {
            JWSSigner signer = new RSASSASigner((RSAPrivateKey) keyPair.getPrivate());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userId)
                    .issuer("mockio-gateway")
                    .claim("roles", roles)
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plusSeconds(3600)))
                    .build();

            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .keyID("mockio-internal")
                    .type(JOSEObjectType.JWT)
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(signer);

            return signedJWT.serialize();

        } catch (Exception e) {
            throw new RuntimeException("JWT 생성 실패", e);
        }
    }

}