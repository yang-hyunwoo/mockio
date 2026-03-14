package com.mockio.interview_service.controller;

import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.interview_service.dto.response.InterviewListResponse;
import com.mockio.interview_service.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interview/v1")
@RequiredArgsConstructor
public class InterviewReadController {

    private final InterviewService interviewService;
    private final MessageUtil messageUtil;

    @GetMapping("/main/list")
    public ResponseEntity<Response<InterviewListResponse>> getInterviewMainList(@CurrentSubject Long userId) {
        return Response.ok(messageUtil.getMessage("response.read"), interviewService.getInterviewList(userId));
    }

}
