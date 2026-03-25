package com.mockio.core_service.util;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.user.constant.error.UserErrorEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.mockio.core_service.user.constant.error.UserErrorEnum.MANY_LATE_EMAIL;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public void checkRateLimit(String email) {

        String key = "password-reset:email:" + email;

        Boolean exists = redisTemplate.hasKey(key);

        if (Boolean.TRUE.equals(exists)) {
            throw new CustomApiException(MANY_LATE_EMAIL.getHttpStatus(),
                    MANY_LATE_EMAIL,
                    MANY_LATE_EMAIL.getMessage());
        }

        // 1분 동안 key 생성
        redisTemplate.opsForValue().set(key, "1", Duration.ofMinutes(1));
    }
}
