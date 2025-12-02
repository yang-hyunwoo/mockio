package com.mockio.user_service.constant.error;


import com.mockio.common_core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum UserErrorEnum implements ErrorCode {

    DUPLICATE_EMAIL(INTERNAL_SERVER_ERROR.value(), "USER_001", "이미 사용 중인 이메일입니다."),
    INVALID_NICKNAME(BAD_REQUEST.value(), "USER_002", "닉네임 형식이 올바르지 않습니다."),;

    private final int httpStatus;
    private final String code;
    private final String message;



}
