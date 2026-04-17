package com.mockio.support_service.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

public record CommentSaveReturnResponse(

        @Schema(description = "댓글 ID", example = "1")
        Long id,

        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "닉네임", example = "홍길두")
        String authorNickname,

        @Schema(description = "내용", example = "댓글 내용입니다.")
        String content,

        @Schema(description = "생성일", example = "yyy-mm-dd hh:mm:sss")
        OffsetDateTime createdAt,

        @Schema(description = "삭제 여부", example = "true")
        boolean deleted,

        @Schema(description = "대댓글", example = "[]")
        List<String> children
) {
}
