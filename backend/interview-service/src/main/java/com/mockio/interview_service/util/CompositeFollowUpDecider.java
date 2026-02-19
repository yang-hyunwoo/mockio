package com.mockio.interview_service.util;

import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.dto.request.InterviewAnswerRequest;
import com.mockio.interview_service.util.followup.FollowUpDecider;
import com.mockio.interview_service.util.followup.FollowUpDecision;
import com.mockio.interview_service.util.followup.RuleBasedFollowUpDecider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class CompositeFollowUpDecider implements FollowUpDecider {

    private final RuleBasedFollowUpDecider ruleDecider;
    private final DeepDiveGate deepDiveGate;
    private final AiDeepDiveFollowUpDecider aiDeepDiveDecider;

    @Override
    public FollowUpDecision decide(InterviewQuestion question, InterviewAnswerRequest req, Interview interview) {

        FollowUpDecision rule = ruleDecider.decide(question, req, interview);
        if (rule.askFollowUp()) {
            return rule; // askNormal
        }

        if (!deepDiveGate.shouldCallAiForDeepDive(interview, req)) {
            return FollowUpDecision.skip("DEEPDIVE_GATE_FALSE");
        }

        //AI 호출
        return aiDeepDiveDecider.decide(question, req, interview);
    }

}