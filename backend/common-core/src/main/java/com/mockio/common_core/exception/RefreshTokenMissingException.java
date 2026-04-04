package com.mockio.common_core.exception;

/**
 * Refresh Token 미 존재 시 발생하는 예외.
 *
 */

public class RefreshTokenMissingException extends RuntimeException {
  public RefreshTokenMissingException(String message) {
    super(message);
  }
}
