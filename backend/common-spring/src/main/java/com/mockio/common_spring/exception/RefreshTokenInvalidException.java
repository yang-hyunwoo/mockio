package com.mockio.common_spring.exception;


public class RefreshTokenInvalidException extends RuntimeException {
  public RefreshTokenInvalidException(String message, Throwable cause) {
    super(message, cause);
  }
}
