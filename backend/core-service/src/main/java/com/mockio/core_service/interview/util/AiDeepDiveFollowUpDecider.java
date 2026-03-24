package com.mockio.core_service.interview.util;

import com.mockio.core_service.interview.domain.Interview;
import com.mockio.core_service.interview.domain.InterviewQuestion;
import com.mockio.core_service.interview.dto.request.InterviewAnswerRequest;
import com.mockio.core_service.interview.util.followup.FollowUpDecider;
import com.mockio.core_service.interview.util.followup.FollowUpDecision;
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
