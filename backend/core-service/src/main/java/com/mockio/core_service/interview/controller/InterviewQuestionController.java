package com.mockio.core_service.interview.controller;

import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.core_service.interview.dto.request.RetryInterviewRequest;
import com.mockio.core_service.interview.dto.request.StartInterviewRequest;
import com.mockio.core_service.interview.dto.response.InterviewQuestionReadResponse;
import com.mockio.core_service.interview.service.InterviewQuestionService;
import com.mockio.core_service.interview.service.InterviewQuestionTxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "면접 프로세스")
@RestController
@RequestMapping("/api/interview/v1")
@RequiredArgsConstructor
public class InterviewQuestionController {

    private final InterviewQuestionService interviewQuestionService;
    private final MessageUtil messageUtil;
    private final InterviewQuestionTxService interviewQuestionTxService;

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "면접 질문 생성")
    @PostMapping("/interviews/start-interview")
    public ResponseEntity<Response<InterviewQuestionReadResponse>> startInterview(@CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
                                                                                  @RequestBody @Valid StartInterviewRequest request
    ) {
        return Response.ok(messageUtil.getMessage("response.read"),
                interviewQuestionService.startInterview(userId, request));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "면접 질문 생성2")
    @PostMapping("/interviews/{interviewId}/questions:generate")
    public ResponseEntity<Response<InterviewQuestionReadResponse>> generateQuestions(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
            @PathVariable @Parameter(description = "면접ID", example = "1") Long interviewId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"),
                interviewQuestionService.generateAndSaveQuestions(interviewId, userId));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "면접 질문 조회")
    @GetMapping("/interviews/{interviewId}/questions")
    public ResponseEntity<Response<InterviewQuestionReadResponse>> getQuestions(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
            @PathVariable @Parameter(description = "면접ID", example = "1") Long interviewId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"),
                interviewQuestionTxService.getQuestions(interviewId,userId)
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "면접 질문 재 생성")
    @PostMapping("/interviews/retry-interview")
    public ResponseEntity<Response<InterviewQuestionReadResponse>> retryInterview(@CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
                                                                                  @RequestBody @Valid RetryInterviewRequest request
    ) {
       return Response.ok(messageUtil.getMessage("response.read"),interviewQuestionService.retryInterview(userId,request));
    }

}
