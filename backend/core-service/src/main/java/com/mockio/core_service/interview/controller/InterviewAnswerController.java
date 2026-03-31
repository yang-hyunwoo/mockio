package com.mockio.core_service.interview.controller;

import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.core_service.interview.dto.request.InterviewAnswerRequest;
import com.mockio.core_service.interview.dto.response.FeedbackDetailResponse;
import com.mockio.core_service.interview.dto.response.InterviewQuestionAnswerDetailResponse;
import com.mockio.core_service.interview.dto.response.InterviewQuestionReadResponse;
import com.mockio.core_service.interview.dto.response.SttResponse;
import com.mockio.core_service.interview.service.InterviewAnswerService;
import com.mockio.core_service.interview.service.InterviewFacadeService;
import com.mockio.core_service.interview.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/interview/v1")
@RequiredArgsConstructor
public class InterviewAnswerController {

    private final InterviewAnswerService interviewAnswerService;
    private final InterviewFacadeService interviewFacadeService;
    private final InterviewService interviewService;
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

    @PatchMapping("/exit/{interviewId}")
    public ResponseEntity<Response<Void>> interviewEnd(
            @CurrentSubject Long userId,
            @PathVariable Long interviewId
    ) {
        interviewService.interviewEnd(userId,interviewId);
        return Response.update(messageUtil.getMessage("response.update"));
    }

    @PatchMapping("/exit")
    public ResponseEntity<Response<Void>> activeInterviewEnd(
            @CurrentSubject Long userId
    ) {
        interviewService.activeInterviewEnd(userId);
        return Response.update(messageUtil.getMessage("response.update"));
    }

    @PostMapping(value = "/answer/stt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<SttResponse>> aiStt(@RequestPart("file") MultipartFile multipartFile,
                                                       @RequestParam("interviewId") Long interviewId,
                                                       @CurrentSubject Long userId) {

        return Response.create(messageUtil.getMessage("response.create"), interviewAnswerService.aiStt(multipartFile, interviewId, userId));

    }

}
