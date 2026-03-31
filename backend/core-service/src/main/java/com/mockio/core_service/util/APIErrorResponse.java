package com.mockio.core_service.util;

public record APIErrorResponse(
        String resultCode,
        Integer httpCode,
        String message,
        String errCode,
        String errCodeMsg,
        Object data,
        String timestamp
) {}
