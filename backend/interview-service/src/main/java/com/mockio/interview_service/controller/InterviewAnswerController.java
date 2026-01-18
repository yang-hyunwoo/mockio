package com.mockio.interview_service.controller;

import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.interview_service.dto.request.InterviewAnswerRequest;
import com.mockio.interview_service.dto.response.InterviewQuestionReadResponse;
import com.mockio.interview_service.service.InterviewAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interview/v1")
@RequiredArgsConstructor
public class InterviewAnswerController {

    private final InterviewAnswerService interviewAnswerService;
    private final MessageUtil messageUtil;

    @PostMapping("/interviews/answer")
    public ResponseEntity<Response<InterviewQuestionReadResponse>> interviewAnswerSave(
            @CurrentSubject String userId,
            @RequestBody InterviewAnswerRequest interviewAnswerRequest) {
        return Response.create(messageUtil.getMessage("response.create"),interviewAnswerService.interviewAnswerSave(userId, interviewAnswerRequest));
    }

}
