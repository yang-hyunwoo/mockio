package com.mockio.support_service.comment.mapper;

import com.mockio.support_service.comment.domain.Comment;
import com.mockio.support_service.comment.dto.response.CommentPageResDto;
import com.mockio.support_service.comment.dto.response.CommentSaveReturnResponse;

import java.util.List;

public class CommentMapper {

    public static CommentSaveReturnResponse fromCommentSave(Comment comment) {
        return new CommentSaveReturnResponse(
                comment.getId(),
                comment.getUserId(),
                comment.getAuthorNickname(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.isDeleted(),
                List.of()
        );
    }

    public static CommentPageResDto toCommentPageResDto(Comment comment ,Long boardAuthorId ,Long userId ,List<CommentPageResDto> children) {
        boolean deleted = comment.isDeleted();

        return new CommentPageResDto(
                comment.getId(),
                comment.getBoardType(),
                comment.getBoardId(),
                comment.getDepth(),
                comment.getUserId(),
                comment.getAuthorNickname(),
                deleted ? "" : comment.getContent(),
                comment.getParentId(),
                comment.isDeleted(),
                comment.getCreatedAt(),
                boardAuthorId.equals(comment.getUserId()),
                userId == null ? false : comment.getUserId().equals(userId),
                children
        );
    }
}
