package com.mockio.core_service.interview.controller;

import com.mockio.common_jpa.dto.PageDto;
import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.core_service.interview.dto.response.InterviewMainListResponse;
import com.mockio.core_service.interview.dto.response.InterviewPageResponse;
import com.mockio.core_service.interview.dto.response.InterviewResultResponse;
import com.mockio.core_service.interview.service.InterviewFacadeService;
import com.mockio.core_service.interview.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "면접 프로세스")
@RestController
@RequestMapping("/api/interview/v1")
@RequiredArgsConstructor
public class InterviewReadController {

    private final InterviewService interviewService;
    private final InterviewFacadeService interviewFacadeService;
    private final MessageUtil messageUtil;

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "면접 메인 목록 조회")
    @GetMapping("/main/list")
    public ResponseEntity<Response<InterviewMainListResponse>> getInterviewMainList(
            @CurrentSubject @Parameter(description = "사용자_ID", example = "1") Long userId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"), interviewService.getInterviewMainList(userId));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "면접 리스트 조회")
    @GetMapping("/list")
    public ResponseEntity<Response<PageDto<InterviewPageResponse>>> getInterviewList(
            @CurrentSubject @Parameter(description = "사용자_ID", example = "1") Long userId,
            @PageableDefault(size = 10, page = 0) @Parameter(description = "페이징", example = "1") Pageable pageable
    ) {
        return Response.ok(messageUtil.getMessage("response.read"), interviewService.getInterviewList(userId, pageable));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "면접 이력 상세 조회")
    @GetMapping("/history/{interviewId}")
    public ResponseEntity<Response<InterviewResultResponse>> getInterviewHistoryDetail(
            @CurrentSubject @Parameter(description = "사용자_ID", example = "1") Long userId,
            @PathVariable @Parameter(description = "면접_ID", example = "1") Long interviewId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"), interviewFacadeService.getInterviewHistoryDetail(interviewId, userId));
    }

}
