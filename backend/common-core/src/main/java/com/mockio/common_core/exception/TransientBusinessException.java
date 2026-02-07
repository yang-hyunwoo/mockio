package com.mockio.common_core.exception;

public class TransientBusinessException extends RuntimeException {

    public TransientBusinessException(String message) {
        super(message);
    }

    public TransientBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
