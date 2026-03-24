package com.mockio.core_service.interview.controller;

import com.mockio.common_ai_contractor.constant.InterviewTrack;
import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.core_service.interview.dto.response.InterviewHistoryPageResponse;
import com.mockio.core_service.interview.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/interview/v1/history")
@RequiredArgsConstructor
public class InterviewHistoryController {

    private final InterviewService interviewService;
    private final MessageUtil messageUtil;

    @GetMapping("/score-list")
    public ResponseEntity<Response<InterviewHistoryPageResponse>> getHistory(@CurrentSubject Long userId,
                                                                             @RequestParam(required = false) InterviewTrack track,
                                                                             @PageableDefault(size = 10, page = 0) Pageable pageable) {

        return Response.ok(messageUtil.getMessage("response.read"), interviewService.getInterviewHistory(userId, track,pageable));
    }

}
