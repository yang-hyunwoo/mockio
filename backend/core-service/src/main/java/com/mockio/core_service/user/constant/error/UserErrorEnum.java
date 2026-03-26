package com.mockio.core_service.user.constant.error;


import com.mockio.common_core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum UserErrorEnum implements ErrorCode {
    USER_NOT_FOUND(NOT_FOUND.value(), "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(CONFLICT.value(), "DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(CONFLICT.value(), "DUPLICATE_NICKNAME", "이미 사용 중인 닉네임입니다."),
    RECAPTCHA_ERROR(BAD_REQUEST.value(), "RECAPTCHA_ERROR", "리캡차 오류가 발생 하였습니다."),
    MANY_LATE_EMAIL(BAD_REQUEST.value(), "MANY_LATE_EMAIL", "너무 많은 요청입니다. 잠시 후 다시 시도하세요."),
    MAIL_LOAD_FAIL(BAD_REQUEST.value(), "MAIL_LOAD_FAIL", "메일 템플릿 로드 실패"),
    MAIL_SEND_FAIL(FORBIDDEN.value(), "MAIL_SEND_FAIL", "메일 발송에 실패 했습니다."),
    PASSWORD_TOKEN_NOT_VALID(BAD_REQUEST.value(), "PASSWORD_TOKEN_NOT_VALID", "비밀번호 토큰 검증에 실패 했습니다."),
    CURRENT_PASSWORD_NOT_MATCH(BAD_REQUEST.value(), "CURRENT_PASSWORD_NOT_MATCH", "비밀번호가 일치하지 않습니다."),
    PASSWORD_NOT_MATCH(BAD_REQUEST.value(), "PASSWORD_NOT_MATCH", "현재 비밀번호가 일치하지 않습니다."),

    RECAPTCHA_REQUIRED(BAD_REQUEST.value(), "RECAPTCHA_REQUIRED", "reCAPTCHA 인증이 필요합니다."),
    RECAPTCHA_INVALID(BAD_REQUEST.value(), "RECAPTCHA_INVALID", "reCAPTCHA 검증에 실패했습니다."),
    RECAPTCHA_VERIFY_FAILED(BAD_GATEWAY.value(), "RECAPTCHA_VERIFY_FAILED", "reCAPTCHA 검증 서버 호출에 실패했습니다."),
    ;;

    private final int httpStatus;
    private final String code;
    private final String message;

}
