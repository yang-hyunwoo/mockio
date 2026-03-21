package com.mockio.feedback_service.controller.internal;

import com.mockio.feedback_service.dto.response.FeedbackDetailResponse;
import com.mockio.feedback_service.dto.response.FeedbackTotalDetailResponse;
import com.mockio.feedback_service.dto.response.InterviewScoreListResponse;
import com.mockio.feedback_service.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback/v1/internal")
@Slf4j
public class InternalFeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("/{answerId}")
    public FeedbackDetailResponse interviewFeedbackRead(@PathVariable Long answerId) {
        log.info("InternalFeedbackController called. answerId={}", answerId);
        return feedbackService.interviewFeedbackRead(answerId);

    }

    @GetMapping("/history/{interviewId}")
    public FeedbackTotalDetailResponse getFeedbackDetail(@PathVariable Long interviewId) {
        log.info("InternalFeedbackController called. interviewId={}", interviewId);
        return feedbackService.getFeedbackDetail(interviewId);
    }

    @GetMapping("/score-history")
    public InterviewScoreListResponse getScoreHistory(@RequestParam List<Long> ids) {
        return feedbackService.getScoreHistory(ids);
    }

}
