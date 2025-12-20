package com.mockio.common_security.util;

import java.util.Optional;

/**
 * common은 "어떻게 조회하는지" 모르고, "조회해줘"만 요청한다.
 * user-service가 UserProfileRepository로 구현한다.
 */
public interface CurrentUserPort<T> {
    Optional<T> findByKeycloakId(String keycloakId);
}
