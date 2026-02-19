package com.mockio.common_core.constant;

import com.mockio.common_core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonErrorEnum implements ErrorCode {
    ERR_000(400,"BAD_REQUEST", "잘못된 요청 입니다."),
    ERR_001(400, "NOT_BLANK", "공백일 수 없습니다."),
    ERR_002(400,"NOT_VALIDATION", "유효성 검사에 실패했습니다."),
    ERR_003(409, "DB_DUPLICATION", "DB에 중복된 값이 있습니다."),
    ERR_004(401, "NOT_AUTH", "인증에 실패 하였습니다."),
    ERR_005(500, "NOT_ENCRYPT", "암호화에 실패 하였습니다."),
    ERR_006(500, "NOT_DECRYPT", "복호화에 실패 하였습니다."),
    ERR_012(404, "NOT_DATA", "존재하지 않는 데이터 입니다."),
    ERR_018(500, "NOT_DB_TYPE", "DB타입 불일치 오류 입니다."),
    ERR_401(401, "UNAUTHORIZED", "인증되지 않았습니다."),
    ERR_TOKEN_EXPIRED(401,"ERR_TOKEN_EXPIRED","토큰이 만료되었습니다."),
    ERR_TOKEN_INVALID(401,"ERR_TOKEN_INVALID","유효하지 않은 토큰입니다."),
    ERR_TOKEN_MISSING(401,"ERR_TOKEN_MISSING","토큰이 없습니다."),
    ERR_FORBIDDEN(401,"ERR_FORBIDDEN","권한이 없습니다."),
    ERR_REFRESH_TOKEN_MISSING(401,"ERR_TOKEN_MISSING","리프레시 토큰이 없습니다."),
    ERR_REFRESH_TOKEN_INVALID(401,"ERR_TOKEN_INVALD","리프레시 토큰이 일치하지 않습니다."),
    ERR_403(403, "NOT_ROLE", "권한이 없습니다."),
    ERR_404(404, "NOT_FOUND", "존재하지 않는 경로입니다."),
    ERR_405(405, "NOT_ALLOW", "허용되지 않은 HTTP 메서드입니다."),
    ERR_500(500, "SERVER_ERROR", "서버 오류가 발생했습니다."),
    ENCRYPTION_FAILED(500, "INTERNAL_SERVER_ERROR", "암호화에 실패했습니다."),
    DECRYPTION_FAILED(400, "BAD_REQUEST", "복호화에 실패했습니다."),
    ILLEGALSTATE(500, "ILLEAGAL_STATE", "500에러."),
    ;

    private final int httpStatus;
    private final String code;
    private final String message;

}
