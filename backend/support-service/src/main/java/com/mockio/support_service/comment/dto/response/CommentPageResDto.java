package com.mockio.support_service.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

public record CommentPageResDto(

        @Schema(description = "댓글 ID", example = "1")
        Long id,

        @Schema(description = "게시판 타입", example = "QUESTION_BOARD")
        String boardType,

        @Schema(description = "게시판 ID", example = "1")
        Long boardId,

        @Schema(description = "댓글 댑스", example = "1")
        int depth,

        @Schema(description = "작성자 ID", example = "1")
        Long userId,

        @Schema(description = "작성자 닉네임", example = "홍길두")
        String authorNickname,

        @Schema(description = "댓글", example = "댓글 내용 입니다.")
        String content,

        @Schema(description = "부모 ID", example = "1")
        Long parentId,

        @Schema(description = "삭제", example = "true")
        boolean deleted,

        @Schema(description = "댓글 등록일", example = "yyyy-MM-dd hh:mm")
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm",
                timezone = "Asia/Seoul"
        )
        OffsetDateTime createdAt,

        @Schema(description = "작성자체크", example = "true")
        boolean writerChk,

        @Schema(description = "자기인지 확인", example = "true")
        boolean mine,

        @Schema(description = "대댓글 리스트", example = "[]")
        List<CommentPageResDto> children

) { }
