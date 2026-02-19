package com.mockio.interview_service.controller;

import com.mockio.interview_service.kafka.dto.response.InterviewAnswerDetailResponse;
import com.mockio.interview_service.service.InternalInterviewReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interview/v1/internal")
public class InternalInterviewReadController {

    private final InternalInterviewReadService internalInterviewReadService;

    @GetMapping("/interview-answers/{answerId}")
    public InterviewAnswerDetailResponse getAnswer(@PathVariable Long answerId) {

        return internalInterviewReadService.getInterviewDetail(answerId);
    }

    @GetMapping("/interview-all/{interviewId}")
    public List<InterviewAnswerDetailResponse> getInterviewList(@PathVariable Long interviewId) {
        return internalInterviewReadService.getInterviewList(interviewId);
    }

}
