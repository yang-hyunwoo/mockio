package com.mockio.common_spring.exception;

import com.mockio.common_core.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomApiFieldException extends RuntimeException{

    private final HttpStatus status;

    private final String field;

    private final ErrorCode errorEnum;

    public CustomApiFieldException(HttpStatus status,
                                   String message,
                                   ErrorCode errorEnum,
                                   String field
    ) {
        super(message);
        this.status = status;
        this.field = field;
        this.errorEnum = errorEnum;
    }

}
