package com.mockio.auth_service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.dto.AuthSession;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class AuthSessionStore {

    private final StringRedisTemplate redis;
    private final ObjectMapper om;

    public AuthSessionStore(StringRedisTemplate redis, ObjectMapper om) {
        this.redis = redis;
        this.om = om;
    }

    private String key(String sessionId) {
        return "auth:session:" + sessionId;
    }

    public void save(String sessionId, AuthSession session, Duration ttl) {
        try {
            String value = om.writeValueAsString(session);
            redis.opsForValue().set(key(sessionId), value, ttl);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize AuthSession", e);
        }
    }

    public Optional<AuthSession> find(String sessionId) {
        String value = redis.opsForValue().get(key(sessionId));
        if (value == null) return Optional.empty();
        try {
            return Optional.of(om.readValue(value, AuthSession.class));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize AuthSession", e);
        }
    }

    public void delete(String sessionId) {
        redis.delete(key(sessionId));
    }

    public void extendTtl(String sessionId, Duration ttl) {
        redis.expire(key(sessionId), ttl);
    }
}
