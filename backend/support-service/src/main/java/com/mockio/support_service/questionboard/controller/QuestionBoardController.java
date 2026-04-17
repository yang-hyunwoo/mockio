package com.mockio.support_service.questionboard.controller;


import com.mockio.common_jpa.dto.PageDto;
import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.support_service.questionboard.dto.request.QuestionBoardCreateRequest;
import com.mockio.support_service.questionboard.dto.request.QuestionBoardDeleteRequest;
import com.mockio.support_service.questionboard.dto.request.QuestionBoardListRequest;
import com.mockio.support_service.questionboard.dto.request.QuestionBoardUpdateRequest;
import com.mockio.support_service.questionboard.dto.response.QuestionBoardDetailResponse;
import com.mockio.support_service.questionboard.dto.response.QuestionBoardDslListResponse;
import com.mockio.support_service.questionboard.dto.response.QuestionBoardListResponse;
import com.mockio.support_service.questionboard.dto.response.QuestionBoardUpdateDetailResponse;
import com.mockio.support_service.questionboard.service.QuestionBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questionboard/v1")
@RequiredArgsConstructor
@Tag(name = "면접 공유 게시판",
        description = """
                면접 공유 게시판 관련 API입니다.
                
                - 면접 공유 게시판 조회
                - 면접 공유 게시판 게시글 등록
                - 면접 공유 게시판 게시글 수정
                - 면접 공유 게시판 게시글 삭제
                """
)
@Slf4j
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;
    private final MessageUtil messageUtil;

    @Operation(summary = "면접 공유 게시판 목록 조회")
    @GetMapping("public/list")
    public ResponseEntity<Response<PageDto<QuestionBoardDslListResponse>>>  getList(QuestionBoardListRequest req) {
        return Response.ok(messageUtil.getMessage("response.read"), questionBoardService.getQuestionBoardList(req));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "면접 공유 게시판 등록 데이터 조회")
    @GetMapping("/create/setting")
    public ResponseEntity<Response<QuestionBoardListResponse>> createQuestionBoardSetting(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
            @RequestParam(required = false) Long interviewId,
            @RequestParam(required = false) Long questionId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"), questionBoardService.createQuestionBoardSetting(userId, interviewId,questionId));
    }


    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "면접 공유 게시판 등록")
    @PostMapping("/create")
    public void createQuestionBoard(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
            @RequestBody @Valid QuestionBoardCreateRequest req
    ) {
        questionBoardService.createQuestionBoard(userId,req);

    }

    @Operation(summary = "면접 공유 게시판 상세 조회")
    @GetMapping("/public/{questionBoardId}/detail")
    public ResponseEntity<Response<QuestionBoardDetailResponse>> getPublicQuestionBoardDetail(
            @PathVariable Long questionBoardId,
            @CurrentSubject(required = false) @Parameter(description = "사용자ID", example = "1") Long userId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"), questionBoardService.getPublicQuestionBoardDetail(questionBoardId, userId));
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "면접 공유 게시판 수정 상세 조회")
    @GetMapping("/{questionBoardId}/detail")
    public ResponseEntity<Response<QuestionBoardUpdateDetailResponse>> getQuestionBoardDetail(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
            @PathVariable Long questionBoardId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"), questionBoardService.getQuestionBoardDetail(userId, questionBoardId));
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "면접 공유 게시판 게시글 수정")
    @PatchMapping("/update")
    public ResponseEntity<Response<Void>> updateQuestionBoard(
        @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
        @RequestBody @Valid QuestionBoardUpdateRequest req
    ) {
        questionBoardService.updateQuestionBoard(userId,req);
        return Response.update(messageUtil.getMessage("response.update"));
    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "면접 공유 게시판 게시글 삭제")
    @PatchMapping("/delete")
    public ResponseEntity<Response<Void>> deleteQuestionBoard(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
            @RequestBody @Valid QuestionBoardDeleteRequest req
    ) {
        questionBoardService.deleteQuestionBoard(userId, req);
        return Response.delete(messageUtil.getMessage("response.delete"));

    }

}
