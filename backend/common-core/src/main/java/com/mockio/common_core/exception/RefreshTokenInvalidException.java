package com.mockio.common_core.exception;


public class RefreshTokenInvalidException extends RuntimeException {
  public RefreshTokenInvalidException(String message, Throwable cause) {
    super(message, cause);
  }
}
