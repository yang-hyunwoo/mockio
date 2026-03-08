package com.mockio.feedback_service.controller.internal;

import com.mockio.feedback_service.dto.response.FeedbackDetailResponse;
import com.mockio.feedback_service.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback/v1/internal")
@Slf4j
public class InternalFeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("/{answerId}")
    public FeedbackDetailResponse getFeedbackDetail(@PathVariable Long answerId) {
        log.info("InternalFeedbackController called. answerId={}", answerId);
        return feedbackService.interviewFeedbackRead(answerId);

    }

}
