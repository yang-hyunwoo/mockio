package com.mockio.common_spring.exception;

import com.mockio.common_core.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomApiException extends RuntimeException{

    private final HttpStatus status;
    private final ErrorCode errorEnum;

    public CustomApiException(HttpStatus status,
                              ErrorCode errorEnum,
                              String message
    ) {
        super(message);
        this.status = status;
        this.errorEnum = errorEnum;
    }

}
