package com.mockio.interview_service.controller;

import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.interview_service.dto.response.InterviewQuestionReadResponse;
import com.mockio.interview_service.service.InterviewQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/interview/v1")
@RequiredArgsConstructor
public class InterviewQuestionController {

    private final InterviewQuestionService interviewQuestionService;
    private final MessageUtil messageUtil;

    @PostMapping("/interviews/current")
    public ResponseEntity<Response<Long>> generateInterview(@CurrentSubject String userId) {
        return Response.ok(messageUtil.getMessage("response.read"),
                interviewQuestionService.generateInterview(userId));
    }

    @PostMapping("/interviews/{interviewId}/questions:generate")
    public ResponseEntity<Response<InterviewQuestionReadResponse>> generateQuestions(
            @CurrentSubject String userId,
            @PathVariable Long interviewId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"),
                interviewQuestionService.generateAndSaveQuestions(interviewId, userId));
    }

    @GetMapping("/interviews/{interviewId}/questions")
    public ResponseEntity<Response<InterviewQuestionReadResponse>> getQuestions(
            @CurrentSubject String userId,
            @PathVariable Long interviewId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"),
                interviewQuestionService.getQuestions(interviewId,userId)
        );
    }

}
