package com.mockio.user_service.constant.error;


import com.mockio.common_core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum UserErrorEnum implements ErrorCode {
    DUPLICATE_EMAIL(CONFLICT.value(), "DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(CONFLICT.value(), "DUPLICATE_NICKNAME", "이미 사용 중인 닉네임입니다."),
    RECAPTCHA_ERROR(BAD_REQUEST.value(), "RECAPTCHA_ERROR", "리캡차 오류가 발생 하였습니다."),
    ;

    private final int httpStatus;
    private final String code;
    private final String message;

}
