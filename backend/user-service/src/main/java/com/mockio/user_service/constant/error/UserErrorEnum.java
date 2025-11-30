package com.mockio.user_service.constant.error;


import com.mockio.common_core.error.ErrorCode;


public enum UserErrorEnum implements ErrorCode {

    DUPLICATE_EMAIL("USER_001", "이미 사용 중인 이메일입니다."),
    INVALID_NICKNAME("USER_002", "닉네임 형식이 올바르지 않습니다.");

    private final String code;
    private final String message;

    UserErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
