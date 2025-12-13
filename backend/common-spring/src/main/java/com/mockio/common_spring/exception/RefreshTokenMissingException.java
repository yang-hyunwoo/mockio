package com.mockio.common_spring.exception;

public class RefreshTokenMissingException extends RuntimeException {
  public RefreshTokenMissingException(String message) {
    super(message);
  }
}
