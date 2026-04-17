package com.mockio.support_service.comment.service;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.common_jpa.dto.PageDto;
import com.mockio.support_service.comment.client.CoreToCommentClient;
import com.mockio.support_service.comment.domain.Comment;
import com.mockio.support_service.comment.dto.request.CommentCreateRequest;
import com.mockio.support_service.comment.dto.request.CommentDeleteRequest;
import com.mockio.support_service.comment.dto.request.CommentReplyCreateRequest;
import com.mockio.support_service.comment.dto.request.CommentUpdateRequest;
import com.mockio.support_service.comment.dto.response.CommentPageResDto;
import com.mockio.support_service.comment.dto.response.CommentSaveReturnResponse;
import com.mockio.support_service.comment.mapper.CommentMapper;
import com.mockio.support_service.comment.repository.CommentRepository;
import com.mockio.support_service.questionboard.domain.QuestionBoard;
import com.mockio.support_service.questionboard.dto.internal.response.UserInfoResponse;
import com.mockio.support_service.questionboard.service.QuestionBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mockio.support_service.comment.constant.CommentErrorEnum.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final CoreToCommentClient coreToCommentClient;
    private final QuestionBoardService questionBoardService;

    @Transactional(readOnly = true)
    public PageDto<CommentPageResDto> commentList(Long userId,String boardType, Long boardId, Pageable pageable) {
        boolean typeChk = false;
        if (boardType.equals("QUESTION_BOARD")) {
            typeChk = questionBoardService.findQuestionBoardTypeCheck(boardId);
            if (typeChk) {
                QuestionBoard questionBoard = questionBoardService.findQuestionBoard(boardId);
                Page<Comment> parentPage = commentRepository
                        .findByBoardTypeAndBoardIdAndParentIdIsNullOrderByIdDesc(boardType, boardId, pageable);

                List<Long> parentIds = parentPage.getContent().stream()
                        .map(Comment::getId)
                        .toList();

                List<Comment> childComments = parentIds.isEmpty()
                        ? List.of()
                        : commentRepository.findByParentIdInOrderByCreatedAtAsc(parentIds);

                Map<Long, List<CommentPageResDto>> childMap = childComments.stream()
                        .collect(Collectors.groupingBy(
                                Comment::getParentId,
                                Collectors.mapping(
                                        child -> CommentMapper.toCommentPageResDto(
                                                child,
                                                questionBoard.getUserId(),
                                                userId,
                                                List.of()
                                        ),
                                        Collectors.toList()
                                )
                        ));

                return PageDto.of(parentPage, parent ->
                        CommentMapper.toCommentPageResDto(
                                parent,
                                questionBoard.getUserId(),
                                userId,
                                childMap.getOrDefault(parent.getId(), List.of())
                        )
                );

            } else {
                throw new CustomApiException(
                        COMMENT_BOARD_TYPE_ERR.getHttpStatus(),
                        COMMENT_BOARD_TYPE_ERR,
                        COMMENT_BOARD_TYPE_ERR.getMessage()
                );
            }
        } else {
            throw new CustomApiException(
                    COMMENT_BOARD_TYPE_ERR.getHttpStatus(),
                    COMMENT_BOARD_TYPE_ERR,
                    COMMENT_BOARD_TYPE_ERR.getMessage()
            );
        }

    }

    /**
     * 댓글 저장
     * @param userId
     * @param req
     * @return
     */
    public CommentSaveReturnResponse commentSave(Long userId, CommentCreateRequest req) {
        //boardType이랑 BoardId가 맞는지 확인!
        boolean typeChk;
        if(req.boardType().equals("QUESTION_BOARD")) {
            typeChk = questionBoardService.findQuestionBoardTypeCheck(req.boardId());
        } else {
            throw new CustomApiException(
                    COMMENT_BOARD_TYPE_ERR.getHttpStatus(),
                    COMMENT_BOARD_TYPE_ERR,
                    COMMENT_BOARD_TYPE_ERR.getMessage()
            );
        }

        //유저 정보 조회
        if(typeChk) {
            questionBoardService.findQuestionBoardTypeCheck(req.boardId());
            UserInfoResponse userInfoResponse = coreToCommentClient.userDetail(userId);

            //저장
            Comment comment = Comment.createComment(
                    req.boardType(),
                    req.boardId(),
                    userId,
                    userInfoResponse.nickname(),
                    req.content()
            );
            Comment save = commentRepository.save(comment);
            return CommentMapper.fromCommentSave(save);
        } else {
            throw new CustomApiException(
                    COMMENT_BOARD_USER_NOT_FOUND.getHttpStatus(),
                    COMMENT_BOARD_USER_NOT_FOUND,
                    COMMENT_BOARD_USER_NOT_FOUND.getMessage()
            );
        }
    }

    public void commentUpdate(Long userId , CommentUpdateRequest req) {
        Comment comment = null;
        if(req.parentId() == null) {
            comment = commentRepository.findByIdAndBoardTypeAndDepthAndUserIdAndDeleted(
                    req.id(),
                    req.boardType(),
                    req.depth(),
                    userId,
                    false
            ).orElseThrow(
                    () -> new CustomApiException(
                            COMMENT_BOARD_USER_NOT_FOUND.getHttpStatus(),
                            COMMENT_BOARD_USER_NOT_FOUND,
                            COMMENT_BOARD_USER_NOT_FOUND.getMessage()
                    )
            );

        } else {
            comment = commentRepository.findByIdAndBoardTypeAndDepthAndUserIdAndParentIdAndDeleted(
                    req.id(),
                    req.boardType(),
                    req.depth(),
                    userId,
                    req.parentId(),
                    false
            ).orElseThrow(
                    () -> new CustomApiException(
                            COMMENT_BOARD_USER_NOT_FOUND.getHttpStatus(),
                            COMMENT_BOARD_USER_NOT_FOUND,
                            COMMENT_BOARD_USER_NOT_FOUND.getMessage()
                    )
            );
        }
        comment.changeContent(req.content());
    }

    /**
     * 댓글 삭제
     * @param userId
     * @param req
     */
    public void commentDelete(Long userId, CommentDeleteRequest req) {
        Comment comment = null;
        if(req.parentId() == null) {
            comment = commentRepository.findByIdAndBoardTypeAndDepthAndUserIdAndDeleted(
                    req.id(),
                    req.boardType(),
                    req.depth(),
                    userId,
                    false
            ).orElseThrow(
                    () -> new CustomApiException(
                            COMMENT_BOARD_USER_NOT_FOUND.getHttpStatus(),
                            COMMENT_BOARD_USER_NOT_FOUND,
                            COMMENT_BOARD_USER_NOT_FOUND.getMessage()
                    )
            );

        } else {
            comment = commentRepository.findByIdAndBoardTypeAndDepthAndUserIdAndParentIdAndDeleted(
                    req.id(),
                    req.boardType(),
                    req.depth(),
                    userId,
                    req.parentId(),
                    false
            ).orElseThrow(
                    () -> new CustomApiException(
                            COMMENT_BOARD_USER_NOT_FOUND.getHttpStatus(),
                            COMMENT_BOARD_USER_NOT_FOUND,
                            COMMENT_BOARD_USER_NOT_FOUND.getMessage()
                    )
            );
        }
        comment.softDelete();
    }

    public CommentSaveReturnResponse commentReplySave(Long userId, CommentReplyCreateRequest req) {
        UserInfoResponse userInfoResponse = coreToCommentClient.userDetail(userId);


        Comment comment = commentRepository.findByIdAndDeleted(req.parentId(), false)
                .orElseThrow(
                        () -> new CustomApiException(
                                COMMENT_BOARD_NOT_FOUND.getHttpStatus(),
                                COMMENT_BOARD_NOT_FOUND,
                                COMMENT_BOARD_NOT_FOUND.getMessage()
                        )
                );
        Comment replyComment = Comment.createReplyComment(
                comment,
                userId,
                userInfoResponse.nickname(),
                req.content()
        );
        Comment save = commentRepository.save(replyComment);
        return CommentMapper.fromCommentSave(save);
    }

}
