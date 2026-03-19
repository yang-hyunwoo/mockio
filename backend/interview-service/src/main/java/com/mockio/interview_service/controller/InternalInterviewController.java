package com.mockio.interview_service.controller;

import com.mockio.interview_service.dto.response.UserInterviewSettingReadResponse;
import com.mockio.interview_service.kafka.dto.response.InterviewAnswerDetailResponse;
import com.mockio.interview_service.service.InternalInterviewReadService;
import com.mockio.interview_service.service.UserInterviewSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interview/v1/internal")
public class InternalInterviewController {

    private final InternalInterviewReadService internalInterviewReadService;
    private final UserInterviewSettingService userInterviewSettingService;

    @GetMapping("/interview-answer/{answerId}")
    public InterviewAnswerDetailResponse getAnswer(@PathVariable Long answerId) {

        return internalInterviewReadService.getInterviewDetail(answerId);
    }

    @GetMapping("/interview-all/{interviewId}")
    public List<InterviewAnswerDetailResponse> getInterviewList(@PathVariable Long interviewId) {
        return internalInterviewReadService.getInterviewList(interviewId);
    }

    @GetMapping("/setting/{userId}")
    public UserInterviewSettingReadResponse getInterviewSetting(
           @PathVariable Long userId
    ) {
        return userInterviewSettingService.getPreference(userId);
    }

}
