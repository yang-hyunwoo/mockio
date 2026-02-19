package com.mockio.interview_service.util;

import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.dto.request.InterviewAnswerRequest;
import com.mockio.interview_service.util.followup.FollowUpDecider;
import com.mockio.interview_service.util.followup.FollowUpDecision;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AiDeepDiveFollowUpDecider implements FollowUpDecider {

    @Override
    public FollowUpDecision decide(InterviewQuestion question, InterviewAnswerRequest req, Interview interview) {
        String answer = req.answerText() == null ? "" : req.answerText().trim();
        if (answer.isBlank()) return FollowUpDecision.skip("NO_ANSWER");

        return FollowUpDecision.skip("DEEPDIVE_CANDIDATE");
    }

}
