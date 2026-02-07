package com.mockio.common_core.constant;

import com.mockio.common_core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum CommonErrorEnum implements ErrorCode {
    ERR_000(BAD_REQUEST.value(),"BAD_REQUEST", "잘못된 요청 입니다."),
    ERR_001(BAD_REQUEST.value(), "NOT_BLANK", "공백일 수 없습니다."),
    ERR_002(BAD_REQUEST.value(),"NOT_VALIDATION", "유효성 검사에 실패했습니다."),
    ERR_003(CONTINUE.value(), "DB_DUPLICATION", "DB에 중복된 값이 있습니다."),
    ERR_004(UNAUTHORIZED.value(), "NOT_AUTH", "인증에 실패 하였습니다."),
    ERR_005(INTERNAL_SERVER_ERROR.value(), "NOT_ENCRYPT", "암호화에 실패 하였습니다."),
    ERR_006(INTERNAL_SERVER_ERROR.value(), "NOT_DECRYPT", "복호화에 실패 하였습니다."),
    ERR_012(NOT_FOUND.value(), "NOT_DATA", "존재하지 않는 데이터 입니다."),
    ERR_018(INTERNAL_SERVER_ERROR.value(), "NOT_DB_TYPE", "DB타입 불일치 오류 입니다."),
    ERR_401(UNAUTHORIZED.value(), "UNAUTHORIZED", "인증되지 않았습니다."),
    ERR_TOKEN_EXPIRED(UNAUTHORIZED.value(),"ERR_TOKEN_EXPIRED","토큰이 만료되었습니다."),
    ERR_TOKEN_INVALID(UNAUTHORIZED.value(),"ERR_TOKEN_INVALID","유효하지 않은 토큰입니다."),
    ERR_TOKEN_MISSING(UNAUTHORIZED.value(),"ERR_TOKEN_MISSING","토큰이 없습니다."),
    ERR_FORBIDDEN(UNAUTHORIZED.value(),"ERR_FORBIDDEN","권한이 없습니다."),
    ERR_REFRESH_TOKEN_MISSING(UNAUTHORIZED.value(),"ERR_TOKEN_MISSING","리프레시 토큰이 없습니다."),
    ERR_REFRESH_TOKEN_INVALID(UNAUTHORIZED.value(),"ERR_TOKEN_INVALD","리프레시 토큰이 일치하지 않습니다."),
    ERR_403(FORBIDDEN.value(), "NOT_ROLE", "권한이 없습니다."),
    ERR_404(NOT_FOUND.value(), "NOT_FOUND", "존재하지 않는 경로입니다."),
    ERR_405(METHOD_NOT_ALLOWED.value(), "NOT_ALLOW", "허용되지 않은 HTTP 메서드입니다."),
    ERR_500(INTERNAL_SERVER_ERROR.value(), "SERVER_ERROR", "서버 오류가 발생했습니다."),
    ENCRYPTION_FAILED(INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR", "암호화에 실패했습니다."),
    DECRYPTION_FAILED(BAD_REQUEST.value(), "BAD_REQUEST", "복호화에 실패했습니다."),
    ILLEGALSTATE(INTERNAL_SERVER_ERROR.value(), "ILLEAGAL_STATE", "500에러."),
    ;
    private final int httpStatus;
    private final String code;
    private final String message;


}
