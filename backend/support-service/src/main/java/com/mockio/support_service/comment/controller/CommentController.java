package com.mockio.support_service.comment.controller;

import com.mockio.common_jpa.dto.PageDto;
import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.support_service.comment.dto.request.CommentCreateRequest;
import com.mockio.support_service.comment.dto.request.CommentDeleteRequest;
import com.mockio.support_service.comment.dto.request.CommentReplyCreateRequest;
import com.mockio.support_service.comment.dto.request.CommentUpdateRequest;
import com.mockio.support_service.comment.dto.response.CommentPageResDto;
import com.mockio.support_service.comment.dto.response.CommentSaveReturnResponse;
import com.mockio.support_service.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/comment/v1")
@RequiredArgsConstructor
@Tag(name = "댓글",
        description = """
                댓글 관련 API입니다.
                
                - 댓글 조회
                - 댓글 등록
                - 댓글 수정
                - 댓글 삭제
                """
)
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final MessageUtil messageUtil;

    @Operation(summary = "댓글 조회")
    @GetMapping("/public/{boardType}/{boardId}/list")
    public ResponseEntity<Response<PageDto<CommentPageResDto>>> commentList(
            @CurrentSubject(required = false) @Parameter(description = "사용자ID", example = "1") Long userId,
            @PathVariable @Parameter(description = "게시판 타입", example = "QUESTION_BOARD") String boardType,
            @PathVariable @Parameter(description = "게시판 ID", example = "1") Long boardId,
            Pageable pageable
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication = " + authentication);
        System.out.println("principal = " + (authentication != null ? authentication.getPrincipal() : null));
        return Response.ok(messageUtil.getMessage("response.read"), commentService.commentList(userId, boardType, boardId, pageable));
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "댓글 등록")
    @PostMapping("/create")
    public ResponseEntity<Response<CommentSaveReturnResponse>> commentSave(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
            @RequestBody @Valid CommentCreateRequest req
    ) {
        return Response.create(messageUtil.getMessage("response.create"), commentService.commentSave(userId,req));

    }

    //수정
    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "댓글 삭제")
    @PatchMapping("/update")
    public ResponseEntity<Response<Void>> commentUpdate(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
            @RequestBody @Valid CommentUpdateRequest req
    ) {
        commentService.commentUpdate(userId,req);
        return Response.update(messageUtil.getMessage("response.update"));
    }

    /**
     * 댓글 삭제
     * @param userId
     * @param req
     * @return
     */
    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "댓글 삭제")
    @PatchMapping("/delete")
    public ResponseEntity<Response<Void>> commentDelete(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
            @RequestBody @Valid CommentDeleteRequest req
    ) {
        commentService.commentDelete(userId, req);
        return Response.delete(messageUtil.getMessage("response.delete"));
    }

    //대댓글 등
    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "대댓글 등록")
    @PostMapping("/reply/create")
    public ResponseEntity<Response<CommentSaveReturnResponse>> commentReplySave(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
            @RequestBody @Valid CommentReplyCreateRequest req
    ) {
        return Response.create(messageUtil.getMessage("response.create"), commentService.commentReplySave(userId,req));

    }

}
