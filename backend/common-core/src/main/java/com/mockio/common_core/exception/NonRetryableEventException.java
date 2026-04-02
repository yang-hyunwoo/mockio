package com.mockio.common_core.exception;

/**
 * 이벤트 처리 중 발생한 예외 중, 재시도하지 않아야 하는 경우를 나타내는 예외.
 *
 * 해당 예외는 비즈니스적으로 재시도해도 동일하게 실패하거나,
 * 재시도가 불필요한 경우에 사용된다.
 *
 * 주로 메시지 큐(Kafka 등) 소비 로직에서 사용되며,
 * 재시도 대신 로그 기록 또는 DLQ(Dead Letter Queue)로 전송하기 위한 용도로 활용된다.
 */

public class NonRetryableEventException extends RuntimeException {

    public NonRetryableEventException(String message) {
        super(message);
    }

    public NonRetryableEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
