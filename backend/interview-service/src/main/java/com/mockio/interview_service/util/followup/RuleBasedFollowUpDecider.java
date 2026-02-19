package com.mockio.interview_service.util.followup;

import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.dto.request.InterviewAnswerRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuleBasedFollowUpDecider implements FollowUpDecider {

    private static final int MIN_LEN = 80;
    private static final List<String> BANNED = List.of("모르겠", "대충", "그냥", "잘 모르");

    @Override
    public FollowUpDecision decide(InterviewQuestion question, InterviewAnswerRequest req, Interview interview) {

        String text = req.answerText() == null ? "" : req.answerText().trim();

        if (text.length() < MIN_LEN) {
            return FollowUpDecision.askNormal("답변이 짧음");
        }

        for (String kw : BANNED) {
            if (text.contains(kw)) {
                return FollowUpDecision.askNormal("모른다는 단어가 포함:" + kw);
            }
        }

        return FollowUpDecision.skip("RULE_PASS");
    }

}