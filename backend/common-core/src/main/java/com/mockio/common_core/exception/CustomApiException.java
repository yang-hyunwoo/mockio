package com.mockio.common_core.exception;

/**
 * API 요청 처리 중 발생하는 비즈니스/도메인 예외를 표현하는 커스텀 예외 클래스.
 *
 * HTTP 상태 코드({ status})와 내부 에러 코드({ ErrorCode})를 함께 포함하여,
 * 예외 발생 시 클라이언트에게 일관된 에러 응답을 제공하기 위해 사용된다.
 *
 * 주로 서비스 계층에서 발생시키며, 글로벌 예외 처리기(@ControllerAdvice)에서
 * 해당 정보를 기반으로 API 응답으로 변환된다.
 */

import com.mockio.common_core.error.ErrorCode;
import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException{

    private final int status;
    private final ErrorCode errorEnum;

    public CustomApiException(int status,
                              ErrorCode errorEnum,
                              String message
    ) {
        super(message);
        this.status = status;
        this.errorEnum = errorEnum;
    }

}
