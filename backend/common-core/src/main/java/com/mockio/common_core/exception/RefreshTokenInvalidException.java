package com.mockio.common_core.exception;

/**
 * Refresh Token 검증 실패 시 발생하는 예외.
 *
 * 토큰이 만료되었거나, 위조되었거나, 서버에 저장된 값과 일치하지 않는 경우 등
 * 유효하지 않은 Refresh Token이 사용될 때 발생한다.
 *
 * 주로 토큰 재발급 과정에서 사용되며,
 * 인증 실패 응답(401 Unauthorized)으로 처리된다.
 */

public class RefreshTokenInvalidException extends RuntimeException {
  public RefreshTokenInvalidException(String message, Throwable cause) {
    super(message, cause);
  }
}
