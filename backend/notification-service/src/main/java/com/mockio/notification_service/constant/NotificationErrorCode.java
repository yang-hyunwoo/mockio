package com.mockio.notification_service.constant;

import com.mockio.common_core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationErrorCode implements ErrorCode {

    NOTIFICATION_FORBIDDEN(403, "NOTIFICATION_FORBIDDEN", "해당 알림에 접근 권한이 없습니다."),

    ;

    private final int httpStatus;
    private final String code;
    private final String message;

}