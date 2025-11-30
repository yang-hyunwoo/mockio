package com.mockio.common_spring.exception;

import com.mockio.common_core.error.ErrorCode;
import com.mockio.common_spring.util.response.ValidationErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class CustomApiFieldListException extends RuntimeException {

    private final HttpStatus status;

    private final ErrorCode errorEnum;

    private final List<ValidationErrorResponse> errors;

    public CustomApiFieldListException(HttpStatus status,
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
