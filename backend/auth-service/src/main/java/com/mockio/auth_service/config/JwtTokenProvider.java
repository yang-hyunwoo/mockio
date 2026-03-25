package com.mockio.auth_service.config;

import com.mockio.auth_service.dto.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.private-key}")
    private String privateKeyPem;

    @Value("${jwt.public-key}")
    private String publicKeyPem;

    @Value("${jwt.access-token-expire-time}")
    private long accessTokenExpire;

    @Value("${jwt.refresh-token-expire-time}")
    private long refreshTokenExpire;

    public String createAccessToken(Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpire);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setHeaderParam("kid", "mockio-auth-key")
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    public String createRefreshToken(LoginUser loginUser) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpire);

        return Jwts.builder()
                .setSubject(String.valueOf(loginUser.getUserId()))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setHeaderParam("kid", "mockio-auth-key")
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long ignoreTokenValid(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.getSubject());
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private PublicKey getPublicKey() {
        try {
            String key = publicKeyPem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Public key 로딩 실패", e);
        }
    }

    private PrivateKey getPrivateKey() {
        try {
            String key = privateKeyPem
                    .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replace("-----END RSA PRIVATE KEY-----", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Private key 로딩 실패", e);
        }
    }

}
