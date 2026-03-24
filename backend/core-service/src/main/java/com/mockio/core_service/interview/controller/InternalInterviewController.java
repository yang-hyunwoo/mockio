package com.mockio.core_service.interview.controller;

import com.mockio.core_service.interview.dto.response.InterviewUserInterviewSettingReadResponse;
import com.mockio.core_service.interview.kafka.dto.response.InternalInterviewAnswerDetailResponse;
import com.mockio.core_service.interview.service.InternalInterviewReadService;
import com.mockio.core_service.interview.service.InterviewAnswerService;
import com.mockio.core_service.interview.service.UserInterviewSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interview/v1/internal")
public class InternalInterviewController {

    private final InternalInterviewReadService internalInterviewReadService;
    private final UserInterviewSettingService userInterviewSettingService;
    private final InterviewAnswerService answerService;
    @GetMapping("/interview-answer/{answerId}")
    public InternalInterviewAnswerDetailResponse getAnswer(@PathVariable Long answerId) {

        return internalInterviewReadService.getInterviewDetail(answerId);
    }

    @GetMapping("/interview-all/{interviewId}")
    public List<InternalInterviewAnswerDetailResponse> getInterviewList(@PathVariable Long interviewId) {
        return answerService.getInterviewList(interviewId);
    }

    @GetMapping("/setting/{userId}")
    public InterviewUserInterviewSettingReadResponse getInterviewSetting(
           @PathVariable Long userId
    ) {
        return userInterviewSettingService.getPreference(userId);
    }

}
