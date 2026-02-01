package com.mockio.ai_service.controller;


import com.mockio.common_ai_contractor.generator.feedback.FeedbackGenerator;
import com.mockio.common_ai_contractor.generator.feedback.GenerateFeedbackCommand;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedFeedback;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ai/v1/feedback")
@RequiredArgsConstructor
public class AIFeedbackController {

    private final FeedbackGenerator feedbackGenerator;

    @PostMapping("/question")
    public GeneratedFeedback singleFeedback(@RequestBody GenerateFeedbackCommand command) {
        return feedbackGenerator.generate(command);

    }
}
