package com.mockio.common_spring.util.response;

import com.mockio.common_core.error.ErrorCode;

public class ResponseBuilder {

    public static <T> Response<T> buildSuccess(String message,
                                               T data
    ) {
        return Response.successRead(message, data);
    }

    public static Response<String> buildError(int httpCode,
                                              String message,
                                              ErrorCode errorCode
    ) {
        return Response.error(httpCode,errorCode, message);
    }

}
