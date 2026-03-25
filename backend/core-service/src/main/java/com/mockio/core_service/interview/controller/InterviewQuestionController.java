package com.mockio.core_service.interview.controller;

import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.core_service.interview.dto.request.RetryInterviewRequest;
import com.mockio.core_service.interview.dto.request.StartInterviewRequest;
import com.mockio.core_service.interview.dto.response.InterviewQuestionReadResponse;
import com.mockio.core_service.interview.service.InterviewQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/interview/v1")
@RequiredArgsConstructor
public class InterviewQuestionController {

    private final InterviewQuestionService interviewQuestionService;
    private final MessageUtil messageUtil;

    @PostMapping("/interviews/start-interview")
    public ResponseEntity<Response<InterviewQuestionReadResponse>> startInterview(@CurrentSubject Long userId,
                                                                                  @RequestBody StartInterviewRequest request
    ) {
        return Response.ok(messageUtil.getMessage("response.read"),
                interviewQuestionService.startInterview(userId, request));
    }

    @PostMapping("/interviews/{interviewId}/questions:generate")
    public ResponseEntity<Response<InterviewQuestionReadResponse>> generateQuestions(
            @CurrentSubject Long userId,
            @PathVariable Long interviewId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"),
                interviewQuestionService.generateAndSaveQuestions(interviewId, userId));
    }

    @GetMapping("/interviews/{interviewId}/questions")
    public ResponseEntity<Response<InterviewQuestionReadResponse>> getQuestions(
            @CurrentSubject Long userId,
            @PathVariable Long interviewId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"),
                interviewQuestionService.getQuestions(interviewId,userId)
        );
    }

    @PostMapping("/interviews/retry-interview")
    public ResponseEntity<Response<InterviewQuestionReadResponse>> retryInterview(@CurrentSubject Long userId,
                                                                                  @RequestBody RetryInterviewRequest request
    ) {
       return Response.ok(messageUtil.getMessage("response.read"),interviewQuestionService.retryInterview(userId,request));
    }

}
