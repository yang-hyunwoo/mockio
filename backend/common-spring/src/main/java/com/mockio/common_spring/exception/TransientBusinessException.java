package com.mockio.common_spring.exception;

public class TransientBusinessException extends RuntimeException {

    public TransientBusinessException(String message) {
        super(message);
    }

    public TransientBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
