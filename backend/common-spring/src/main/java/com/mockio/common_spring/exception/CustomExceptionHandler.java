package com.mockio.common_spring.exception;

import com.mockio.common_core.exception.*;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_core.exception.ValidationErrorResponse;
import com.mockio.common_spring.util.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

import static com.mockio.common_core.constant.CommonErrorEnum.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class CustomExceptionHandler {

    private final MessageUtil messageUtil;

    /**
     * 일반적인 에러
     * @param e
     * @return
     */
    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<Response<String>> handleCustomApiException(CustomApiException e) {
        log.error("CustomApiException: {}", e.getMessage());
        return Response.error(e.getStatus(), e.getMessage(), e.getErrorEnum(), null);
    }

    //TODO 어떤 방식으로 내려줄지 고민
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<ValidationErrorResponse>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException: {}" , e.getMessage());
        return Response.error(BAD_REQUEST.value(), e.getMessage(), ERR_002, null);
    }

    /**
     * 변수 단일 유효성 에러 리턴
     * @param e
     * @return
     */
    @ExceptionHandler(CustomApiFieldException.class)
    public ResponseEntity<Response<ValidationErrorResponse>> handleCustomApiFieldException(CustomApiFieldException e) {
        log.error("CustomApiFieldException: {}", e.getMessage());
        return Response.validationError(e.getHttpStatus(), e.getMessage(),e.getErrorEnum(),e.getField());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response<ValidationErrorResponse>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException: {}" , e.getMessage());
        return Response.error(BAD_REQUEST.value(), messageUtil.getMessage("error.not.data.type.ok"), ERR_018, null);
    }

    /**
     * 변수 리스트 유효성 에러 리턴
     * @param e
     * @return
     */
    @ExceptionHandler(CustomApiFieldListException.class)
    public ResponseEntity<Response<List<ValidationErrorResponse>>> handleCustomApiFieldListException(CustomApiFieldListException e) {
        log.error("CustomApiFieldListException: {}", e.getMessage());
        return Response.validationErrorList(e.getStatus(), e.getMessage(), e.getErrorEnum(), e.getErrors());
    }

    /**
     * 404 에러
     * @param
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Response<String>> handleNotFound(NoHandlerFoundException ex) {
        log.error("404 에러 발생",ex);
        return Response.error(NOT_FOUND.value(), messageUtil.getMessage("error.not.fount.ok"), ERR_404, null);
    }

    /**
     * 405 에러
     * @param
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Response<String>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        log.error("405 에러 발생",ex);
        return Response.error(METHOD_NOT_ALLOWED.value(), messageUtil.getMessage("error.not.allow.method.ok"), ERR_405, null);
    }

    /**
     * 500 에러
     * @param
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<String>> handleServerError(Exception ex) {
        log.error("500 에러 발생", ex);
        return Response.error(INTERNAL_SERVER_ERROR.value(), messageUtil.getMessage("error.server.ok"), ERR_500, null);
    }

    /**
     * refresh_token 쿠키가 없는 경우: 401
     */
    @ExceptionHandler(RefreshTokenMissingException.class)
    public ResponseEntity<Response<String>> refreshMissing(RefreshTokenMissingException e) {
        return Response.error(UNAUTHORIZED.value(),messageUtil.getMessage("error.refresh.missing"), ERR_REFRESH_TOKEN_MISSING,null);
    }

    /**
     * refresh_token이 무효/만료/invalid_grant 등인 경우: 401
     */
    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<Response<String>> refreshInvalid(RefreshTokenInvalidException e) {
        return Response.error(UNAUTHORIZED.value(),messageUtil.getMessage("error.refresh.invalid"), ERR_REFRESH_TOKEN_INVALID,null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Response<String>> handleNoResourceFound(NoResourceFoundException ex) {
        log.error("404 리소스 없음", ex);
        return Response.error(NOT_FOUND.value(), messageUtil.getMessage("error.not.fount.ok"), ERR_404,null);
    }

}
