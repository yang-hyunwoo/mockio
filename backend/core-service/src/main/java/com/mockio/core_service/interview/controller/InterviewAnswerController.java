package com.mockio.core_service.interview.controller;

import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.core_service.interview.dto.request.InterviewAnswerRequest;
import com.mockio.core_service.interview.dto.response.FeedbackDetailResponse;
import com.mockio.core_service.interview.dto.response.InterviewQuestionAnswerDetailResponse;
import com.mockio.core_service.interview.dto.response.InterviewQuestionReadResponse;
import com.mockio.core_service.interview.dto.response.SttResponse;
import com.mockio.core_service.interview.service.InterviewAnswerService;
import com.mockio.core_service.interview.service.InterviewFacadeService;
import com.mockio.core_service.interview.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "면접 프로세스",
        description = """
                면접 프로세스 관련 API입니다.
                
                - 면접 생성
                - 면접 질문 생성
                - 면접 꼬리 질문 / 딥다이브 질문 생성
                - 면접 이력 조회
                - 면접 상세 조회
                - 면접 세팅 수정
                - 면접 피드백 조회
                - 면접 요약 조회
                
                
                """
)
@RestController
@RequestMapping("/api/interview/v1")
@RequiredArgsConstructor
public class InterviewAnswerController {

    private final InterviewAnswerService interviewAnswerService;
    private final InterviewFacadeService interviewFacadeService;
    private final InterviewService interviewService;
    private final MessageUtil messageUtil;

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "면접 피드백 조회")
    @GetMapping("/{questionId}/feedback")
    public ResponseEntity<Response<FeedbackDetailResponse>> interviewAnswerFeedbackRead(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
            @PathVariable @Parameter(description = "질문ID", example = "1") Long questionId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"), interviewFacadeService.readFeedback(userId, questionId));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "면접 답변 저장")
    @PostMapping("/interviews/answer")
    public ResponseEntity<Response<InterviewQuestionReadResponse>> interviewAnswerSave(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
            @RequestBody @Valid InterviewAnswerRequest interviewAnswerRequest
    ) {
        return Response.create(messageUtil.getMessage("response.create"), interviewAnswerService.interviewAnswerSave(userId, interviewAnswerRequest));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "면접 질문 조회")
    @GetMapping("/interviews/answer/{questionId}")
    public ResponseEntity<Response<InterviewQuestionAnswerDetailResponse>> interviewAnswerRead(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
            @PathVariable @Parameter(description = "질문ID", example = "1") Long questionId
    ) {

        return Response.ok(messageUtil.getMessage("response.read"), interviewAnswerService.interviewAnswerRead(userId, questionId));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "면접 종료")
    @PatchMapping("/exit/{interviewId}")
    public ResponseEntity<Response<Void>> interviewEnd(
            @CurrentSubject @Parameter(description = "ID" ,example = "1") Long userId,
            @PathVariable @Parameter(description = "면접 ID" ,example = "1") Long interviewId
    ) {
        interviewService.interviewEnd(userId,interviewId);
        return Response.update(messageUtil.getMessage("response.update"));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "활성화 된 면접 종료")
    @PatchMapping("/exit")
    public ResponseEntity<Response<Void>> activeInterviewEnd(
            @CurrentSubject @Parameter(description = "ID" ,example = "1") Long userId
    ) {
        interviewService.activeInterviewEnd(userId);
        return Response.update(messageUtil.getMessage("response.update"));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "stt 저장")
    @PostMapping(value = "/answer/stt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<SttResponse>> aiStt(@RequestPart("file") @Parameter(description = "파일", example = "file") MultipartFile multipartFile,
                                                       @RequestParam("interviewId") @Parameter(description = "면접 ID", example = "1") Long interviewId,
                                                       @CurrentSubject @Parameter(description = "ID", example = "1") Long userId
    ) {
        return Response.create(messageUtil.getMessage("response.create"), interviewAnswerService.aiStt(multipartFile, interviewId, userId));
    }

}
