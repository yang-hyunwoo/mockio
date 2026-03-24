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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interview/v1")
@RequiredArgsConstructor
public class InterviewReadController {

    private final InterviewService interviewService;
    private final InterviewFacadeService interviewFacadeService;
    private final MessageUtil messageUtil;

    @GetMapping("/main/list")
    public ResponseEntity<Response<InterviewMainListResponse>> getInterviewMainList(@CurrentSubject Long userId) {
        return Response.ok(messageUtil.getMessage("response.read"), interviewService.getInterviewMainList(userId));
    }

    @GetMapping("/list")
    public ResponseEntity<Response<PageDto<InterviewPageResponse>>> getInterviewList(@CurrentSubject Long userId,
                                                                                     @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return Response.ok(messageUtil.getMessage("response.read"), interviewService.getInterviewList(userId,pageable));
    }

    @GetMapping("/history/{interviewId}")
    public ResponseEntity<Response<InterviewResultResponse>> getInterviewHistoryDetail(@CurrentSubject Long userId,
                                                                                       @PathVariable Long interviewId) {
        return Response.ok(messageUtil.getMessage("response.read"), interviewFacadeService.getInterviewHistoryDetail(interviewId, userId));
    }

}
