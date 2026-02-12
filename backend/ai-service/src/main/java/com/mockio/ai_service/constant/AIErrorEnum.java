package com.mockio.ai_service.constant;

/**
 * AI 서비스에서 발생하는 오류를 표준화하여 정의한 에러 코드 Enum.
 *
 * <p>HTTP 상태 코드, 내부 오류 코드, 사용자에게 노출될 메시지를 함께 관리하며
 * 공통 ErrorCode 인터페이스를 구현하여 전역 예외 처리에서 일관되게 사용된다.</p>
 */

import com.mockio.common_core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum AIErrorEnum implements ErrorCode {

    ILLEGALSTATE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "AI_INTERNAL_ERROR", "AI 처리 중 알 수 없는 오류가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "AI_BAD_REQUEST", "AI 요청 형식이 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "AI_UNAUTHORIZED", "AI 인증에 실패했습니다."),
    RATE_LIMIT(HttpStatus.TOO_MANY_REQUESTS.value(), "AI_RATE_LIMIT", "AI 요청이 너무 많아 잠시 후 다시 시도해 주세요."),
    TEMPORARY_ERROR(HttpStatus.TEMPORARY_REDIRECT.value(),"TEMPORARY_ERROR","AI TEMPORARY_ERROR")
    ;

    private final int httpStatus;
    private final String code;
    private final String message;

}
