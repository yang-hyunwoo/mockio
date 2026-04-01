package com.mockio.core_service.interview.util;

/**
 * deep-dive 질문을 AI로 생성할지 판단하는 정책 메서드
 *
 * 목적:
 * - 사용자의 답변이 "겉핥기 수준"인지 판단
 * - 추가적으로 깊이 있는 질문이 필요한 경우 true 반환
 *
 * 핵심 기준:
 * 1. 답변 길이
 * 2. 난이도 (HARD 이상에서만 deep-dive 허용)
 * 3. 근거/트레이드오프/운영 경험 존재 여부
 * 4. 단순 나열식 답변인지 여부
 */

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.core_service.interview.constant.QuestionType;
import com.mockio.core_service.interview.domain.Interview;
import com.mockio.core_service.interview.dto.request.InterviewAnswerRequest;
import org.springframework.stereotype.Component;


@Component
public class DeepDiveGate {

    private static final int MIN_LEN = 60;

    public boolean shouldCallAiForDeepDive(
            Interview interview,
            InterviewAnswerRequest req,
            QuestionType questionType
    ) {
        if (questionType != QuestionType.FOLLOW_UP) {
            return false;
        }

        String text = req.answerText() == null ? "" : req.answerText().trim();

        if (text.isBlank()) {
            return false;
        }

        if (text.length() < MIN_LEN) {
            return false;
        }

        return interview.getDifficulty() == InterviewDifficulty.HARD;
    }
}

