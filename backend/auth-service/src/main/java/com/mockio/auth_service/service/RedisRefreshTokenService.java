package com.mockio.auth_service.service;

/**
 * Refresh Token을 Redis에 저장 및 관리하는 서비스 클래스
 *
 * 사용자별 Refresh Token을 Redis에 저장하고 조회/삭제/검증 기능을 제공한다.
 * 토큰은 "refresh:{userId}" 형식의 키로 관리되며,
 * TTL(Time To Live)을 설정하여 자동 만료되도록 한다.
 */

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenService {

    private final StringRedisTemplate redisTemplate;
    private static final String PREFIX = "refresh:";

    /**
     * Refresh Token 저장
     *
     * <p>
     * 사용자 ID를 기반으로 Redis에 Refresh Token을 저장하며,
     * 지정된 TTL 동안 유효하도록 설정한다.
     * </p>
     */
    public void save(Long userId, String refreshToken, Duration ttl) {

        redisTemplate.opsForValue().set(
                PREFIX + userId,
                refreshToken,
                ttl
        );
    }

    /**
     * Refresh Token 조회
     *
     * <p>
     * 사용자 ID를 기반으로 Redis에 저장된 Refresh Token을 조회한다.
     * 존재하지 않을 경우 null을 반환한다.
     * </p>
     */
    public String get(Long userId) {
        return redisTemplate.opsForValue().get(PREFIX + userId);
    }

    /**
     * Refresh Token 삭제
     *
     * <p>
     * 로그아웃 시 사용자 ID에 해당하는 Refresh Token을 Redis에서 제거한다.
     * </p>
     */
    public void delete(Long userId) {
        redisTemplate.delete(PREFIX + userId);
    }

}
