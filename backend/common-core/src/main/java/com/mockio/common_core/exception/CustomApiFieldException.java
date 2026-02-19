package com.mockio.common_core.exception;

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
