package com.mockio.core_service.interview.util.followup;

/**
 * follow-up 질문을 AI로 생성할지 판단하는 정책 메서드
 *
 * 목적:
 * - 사용자의 답변이 "겉핥기 수준"인지 판단
 * - 추가적으로 깊이 있는 질문이 필요한 경우 true 반환
 *
 * 핵심 기준:
 * 1. 답변 길이
 * 2. BANNED 문자열 포함
 */

import com.mockio.core_service.interview.domain.Interview;
import com.mockio.core_service.interview.domain.InterviewQuestion;
import com.mockio.core_service.interview.dto.request.InterviewAnswerRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuleBasedFollowUpDecider implements FollowUpDecider {
    private static final List<String> NO_ANSWER_PATTERNS = List.of(
            "모르겠", "잘 모르", "기억이 안"
    );

    @Override
    public FollowUpDecision decide(InterviewQuestion question, InterviewAnswerRequest req, Interview interview) {
        String text = req.answerText() == null ? "" : req.answerText().trim();

        if (text.isBlank()) {
            return FollowUpDecision.skip("EMPTY_ANSWER");
        }

        boolean noAnswer = NO_ANSWER_PATTERNS.stream().anyMatch(text::contains);
        if (noAnswer) {
            return FollowUpDecision.skip("NO_ANSWER");
        }


        return FollowUpDecision.deferToAi("RULE_AMBIGUOUS");
    }
}