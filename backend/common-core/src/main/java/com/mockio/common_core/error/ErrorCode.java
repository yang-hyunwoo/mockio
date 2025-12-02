package com.mockio.common_core.error;

public interface ErrorCode {
    int getHttpStatus();
    String getCode();
    String getMessage();

}
