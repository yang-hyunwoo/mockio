package com.mockio.support_service.file.constant;

import com.mockio.common_core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileErrorCode implements ErrorCode {
    CLOUDINARY_NOT_CONNECT(500, "CLOUDINARY_NOT_CONNECT", "cloudinary 연결 실패"),
    CLOUDINARY_ERROR(500, "CLOUDINARY_ERROR", "cloudinary 서버 오류가 발생했습니다."),
    FILE_NOT_FOUND(400, "FILE_NOT_FOUND", "파일을 찾을 수 없습니다."),
    ;
    private final int httpStatus;
    private final String code;
    private final String message;
}
