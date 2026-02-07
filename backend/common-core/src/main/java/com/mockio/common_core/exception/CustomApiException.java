package com.mockio.common_core.exception;

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
