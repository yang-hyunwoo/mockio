package com.mockio.common_core.exception;

/**
 * 요청 값 검증 과정에서 특정 필드 오류를 표현하는 커스텀 예외 클래스.
 *
 * 어떤 필드({ field})에서 문제가 발생했는지 명시하여,
 * 클라이언트가 에러 원인을 정확히 파악하고 대응할 수 있도록 한다.
 *
 * 주로 유효성 검사(Validation) 또는 입력값 검증 실패 시 발생시키며,
 * 글로벌 예외 처리기(@ControllerAdvice)에서 필드 단위 에러 응답으로 변환된다.
 */

import com.mockio.common_core.error.ErrorCode;
import lombok.Getter;

@Getter
public class CustomApiFieldException extends RuntimeException{

    private final int httpStatus;
    private final String field;
    private final ErrorCode errorEnum;

    public CustomApiFieldException(int status,
                                   String message,
                                   ErrorCode errorEnum,
                                   String field
    ) {
        super(message);
        this.httpStatus = status;
        this.field = field;
        this.errorEnum = errorEnum;
    }

}
