package com.mockio.auth_service.config;

/**
 * JWT 토큰 생성 및 검증을 담당하는 Provider 클래스
 * - RSA 기반 (RS256) 서명 방식 사용
 * - AccessToken / RefreshToken 생성
 * - 토큰 파싱 및 유효성 검증 처리
 *
 * 키 관리:
 * - PrivateKey → 토큰 서명(Sign)
 * - PublicKey → 토큰 검증(Verify)
 *
 * 환경별 키 로딩:
 * - 운영(prod): 외부 경로에서 파일 로드
 * - 개발/로컬: classpath에서 키 로드
 */

import com.mockio.auth_service.dto.LoginUser;
import com.mockio.common_spring.util.EnvironmentProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    @Value("${jwt.private-key-path}")
    private String privateKeyPath;

    @Value("${jwt.public-key-path}")
    private String publicKeyPath;

    @Value("${jwt.access-token-expire-time}")
    private long accessTokenExpire;

    @Value("${jwt.refresh-token-expire-time}")
    private long refreshTokenExpire;

    private final EnvironmentProvider environmentProvider;

    /**
     * Access Token 생성
     * - userId를 subject로 설정
     * - 만료 시간(accessTokenExpire) 적용
     * - RSA PrivateKey로 서명
     * - header에 key id(kid) 포함
     * @param userId 사용자 ID
     * @return JWT Access Token
     */
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

    /**
     * Refresh Token 생성
     * - AccessToken보다 긴 만료 시간 적용
     * - 사용자 식별을 위해 userId를 subject로 사용
     * @param loginUser 로그인 사용자 정보
     * @return JWT Refresh Token
     */
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

    /**
     * 토큰에서 사용자 ID 추출
     * - 내부적으로 claims 파싱 후 subject 반환
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    /**
     * 토큰 유효성 검증
     * - 서명 검증 + 만료 시간 검증 포함
     * - 예외 발생 시 false 반환
     * @param token JWT 토큰
     * @return 유효 여부
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 토큰 유효성 무시하고 사용자 ID 추출
     * - 만료 여부와 관계없이 claims 파싱
     * - (주의) 보안적으로 제한된 상황에서만 사용해야 함
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public Long ignoreTokenValid(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.getSubject());
    }

    /**
     * 토큰 claims 파싱 (공통 내부 메서드)
     * - PublicKey 기반 서명 검증 수행
     * - 성공 시 Claims 반환
     * @param token JWT 토큰
     * @return Claims
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * RSA PublicKey 로드
     * 처리 과정:
     * 1. 파일에서 PEM 형식 문자열 로드
     * 2. 헤더/푸터 제거
     * 3. Base64 디코딩
     * 4. X509EncodedKeySpec 변환
     * 5. PublicKey 객체 생성
     * @return PublicKey (JWT 검증용)
     */
    private PublicKey getPublicKey() {
        try {
            String publicKeyString;
            if(environmentProvider.isProd()) {
                publicKeyString = Files.readString(Paths.get(publicKeyPath));
            } else {
                Resource resource = new ClassPathResource(publicKeyPath);
                publicKeyString= new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            }

            String key = publicKeyString
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
    private PrivateKey getPrivateKey() {
        try {
            String privateKeyString;

            if(environmentProvider.isProd()) {
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
            throw new RuntimeException("Private key 로딩 실패", e);
        }
    }

}
