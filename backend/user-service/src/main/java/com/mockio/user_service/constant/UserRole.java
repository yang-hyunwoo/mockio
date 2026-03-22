package com.mockio.user_service.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
    USER("사용자"),
    ADMIN("관리자")
    ;

    private final String role;
}
