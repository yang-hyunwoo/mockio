package com.mockio.core_service.interview.controller;

import com.mockio.common_ai_contractor.constant.InterviewTrack;
import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.core_service.interview.dto.response.InterviewHistoryPageResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "면접 프로세스")
@RestController
@RequestMapping("/api/interview/v1/history")
@RequiredArgsConstructor
public class InterviewHistoryController {

    private final InterviewService interviewService;
    private final MessageUtil messageUtil;

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "면접 이력 조회")
    @GetMapping("/score-list")
    public ResponseEntity<Response<InterviewHistoryPageResponse>> getHistory(@CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
                                                                             @RequestParam(required = false) @Parameter(description = "면접트랙", example = "HR") InterviewTrack track,
                                                                             @PageableDefault(size = 10, page = 0) @Parameter(description = "페이징", example = "1") Pageable pageable
    ) {
        return Response.ok(messageUtil.getMessage("response.read"), interviewService.getInterviewHistory(userId, track, pageable));
    }

}
