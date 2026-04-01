package com.mockio.auth_service.constant;

/**
 * client 호출 시 사용자 관련 에러 enum
 */

import com.mockio.common_core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum UserErrorEnum implements ErrorCode {
    USER_NOT_FOUND(NOT_FOUND.value(), "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(CONFLICT.value(), "DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다."),
    ILLEGAL_STATE(INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR", "서버 오류가 발생 하였습니다."),
    ;

    private final int httpStatus;
    private final String code;
    private final String message;

}
