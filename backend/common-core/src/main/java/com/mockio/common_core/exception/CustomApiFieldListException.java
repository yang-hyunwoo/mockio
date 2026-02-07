package com.mockio.common_core.exception;

import com.mockio.common_core.error.ErrorCode;
import lombok.Getter;

import java.util.List;

@Getter
public class CustomApiFieldListException extends RuntimeException {

    private final int status;

    private final ErrorCode errorEnum;

    private final List<ValidationErrorResponse> errors;

    public CustomApiFieldListException(int status,
                                       String message,
                                       ErrorCode errorEnum,
                                       List<ValidationErrorResponse> errors
    ) {
        super(message);
        this.status = status;
        this.errorEnum = errorEnum;
        this.errors = errors;
    }

}
