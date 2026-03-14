package com.mockio.interview_service.controller;

import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.interview_service.dto.request.InterviewAnswerRequest;
import com.mockio.interview_service.dto.response.FeedbackDetailResponse;
import com.mockio.interview_service.dto.response.InterviewQuestionAnswerDetailResponse;
import com.mockio.interview_service.dto.response.InterviewQuestionReadResponse;
import com.mockio.interview_service.service.InterviewAnswerService;
import com.mockio.interview_service.service.InterviewFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview/v1")
@RequiredArgsConstructor
public class InterviewAnswerController {

    private final InterviewAnswerService interviewAnswerService;
    private final InterviewFacadeService interviewFacadeService;
    private final MessageUtil messageUtil;

    @GetMapping("/{questionId}/feedback")
    public ResponseEntity<Response<FeedbackDetailResponse>> interviewAnswerFeedbackRead(
            @CurrentSubject Long userId,
            @PathVariable Long questionId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"), interviewFacadeService.readFeedback(userId, questionId));
    }

    @PostMapping("/interviews/answer")
    public ResponseEntity<Response<InterviewQuestionReadResponse>> interviewAnswerSave(
            @CurrentSubject Long userId,
            @RequestBody InterviewAnswerRequest interviewAnswerRequest) {
        return Response.create(messageUtil.getMessage("response.create"),interviewAnswerService.interviewAnswerSave(userId, interviewAnswerRequest));
    }

    @GetMapping("/interviews/answer/{questionId}")
    public ResponseEntity<Response<InterviewQuestionAnswerDetailResponse>> interviewAnswerRead(
            @CurrentSubject Long userId,
            @PathVariable Long questionId) {
        return Response.ok(messageUtil.getMessage("response.read"), interviewAnswerService.interviewAnswerRead(userId, questionId));
    }


}
