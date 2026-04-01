package com.mockio.core_service.interview.util;

import com.mockio.core_service.interview.domain.Interview;
import com.mockio.core_service.interview.domain.InterviewQuestion;
import com.mockio.core_service.interview.dto.request.InterviewAnswerRequest;
import com.mockio.core_service.interview.util.followup.FollowUpDecider;
import com.mockio.core_service.interview.util.followup.FollowUpDecision;
import com.mockio.core_service.interview.util.followup.RuleBasedFollowUpDecider;
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
        if (rule.shouldDeferToAi() || rule.shouldSkip()) {
            return rule; // askNormal
        }
        if (!deepDiveGate.shouldCallAiForDeepDive(interview, req,question.getType())) {
            return FollowUpDecision.skip("DEEPDIVE_GATE_FALSE");
        }

        //AI 호출
        return aiDeepDiveDecider.decide(question, req, interview);
    }

}