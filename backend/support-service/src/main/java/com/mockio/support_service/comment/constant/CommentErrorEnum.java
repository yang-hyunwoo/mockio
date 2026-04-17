package com.mockio.support_service.comment.constant;

import com.mockio.common_core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CommentErrorEnum implements ErrorCode {

    COMMENT_BOARD_TYPE_ERR(HttpStatus.FORBIDDEN.value(),"COMMENT_BOARD_TYPE_ERR","게시글 타입이 올바르지 않습니다."),
    COMMENT_BOARD_USER_NOT_FOUND(HttpStatus.FORBIDDEN.value(),"COMMENT_BOARD_USER_NOT_FOUND","게시글 유저 권한이 없습니다."),
    COMMENT_BOARD_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"COMMENT_BOARD_NOT_FOUND","댓글이 없습니다.")
    ;

    private final int httpStatus;
    private final String code;
    private final String message;
}
