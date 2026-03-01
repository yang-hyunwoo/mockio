package com.mockio.auth_service.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class PkceStore {

    private final StringRedisTemplate redis;

    public PkceStore(StringRedisTemplate redis) {
        this.redis = redis;
    }

    private String key(String state) {
        return "pkce:" + state;
    }

    public void saveVerifier(String state, String verifier) {
        redis.opsForValue().set(key(state), verifier, Duration.ofMinutes(5));
    }

    public String consumeVerifier(String state) {
        String k = key(state);
        String verifier = redis.opsForValue().get(k);
        if (verifier != null) {
            redis.delete(k); // 1회 사용 후 삭제
        }
        return verifier;
    }
}
