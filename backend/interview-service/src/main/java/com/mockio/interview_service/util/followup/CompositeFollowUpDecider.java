package com.mockio.interview_service.util.followup;

import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.dto.request.InterviewAnswerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
@Deprecated
public class CompositeFollowUpDecider implements FollowUpDecider {

    private final RuleBasedFollowUpDecider rule;


    @Override
    public FollowUpDecision decide(InterviewQuestion q, InterviewAnswerRequest req, Interview interview) {

        return rule.decide(q, req, interview);

    }
}
