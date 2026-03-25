package com.mockio.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenService {

    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "refresh:";

    public void save(Long userId, String refreshToken, Duration ttl) {
        String key = PREFIX + userId;

        redisTemplate.opsForValue().set(
                key,
                refreshToken,
                ttl
        );
    }

    public String get(Long userId) {
        return redisTemplate.opsForValue().get(PREFIX + userId);
    }

    public void delete(Long userId) {
        redisTemplate.delete(PREFIX + userId);
    }

    public boolean isValid(Long userId, String refreshToken) {
        String stored = get(userId);
        return stored != null && stored.equals(refreshToken);
    }
}
