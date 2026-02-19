package com.mockio.user_service.constant;

/** 유저 상태 enum
 *  UserStatus
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {
    ACTIVE("활성"),
    SUSPENDED("정지"),
    DELETED("삭제")
    ;

    private final String label;

}
