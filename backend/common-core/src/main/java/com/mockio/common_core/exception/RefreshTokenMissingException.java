package com.mockio.common_core.exception;

public class RefreshTokenMissingException extends RuntimeException {
  public RefreshTokenMissingException(String message) {
    super(message);
  }
}
