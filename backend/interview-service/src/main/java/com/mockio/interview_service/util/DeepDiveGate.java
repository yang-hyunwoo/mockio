package com.mockio.interview_service.util;

import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.dto.request.InterviewAnswerRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeepDiveGate {

    private static final int MIN_LEN = 150;

    private static final List<String> SHALLOW_PATTERNS = List.of(
            "사용했습니다", "적용했습니다", "구현했습니다"
    );

    private static final List<String> CONNECTORS = List.of("그래서", "따라서");

    // 진짜 딥한 근거/트레이드오프 신호
    private static final List<String> REAL_JUSTIFICATIONS = List.of(
            "왜냐하면", "이유는", "트레이드오프", "단점", "대안",
            "비교", "선택", "근거",
            "장애", "실패", "타임아웃", "재시도", "모니터링"
    );

    public boolean shouldCallAiForDeepDive(Interview interview, InterviewAnswerRequest req) {
        String text = req.answerText() == null ? "" : req.answerText().trim();
        if (text.length() < MIN_LEN) return false;

        String difficulty = interview.getDifficulty().name();
        if (!("HARD".equals(difficulty) || "VERY_HARD".equals(difficulty))) return false;

        long shallowCount = SHALLOW_PATTERNS.stream().filter(text::contains).count();
        boolean hasRealJustification = REAL_JUSTIFICATIONS.stream().anyMatch(text::contains);

        // 1) 근거/트레이드오프/운영 신호가 없으면 딥다이브 가치 ↑
        if (!hasRealJustification) return true;

        // 2) 근거 신호는 있어도 “구현/사용 나열”이 많으면 딥다이브 가치 ↑
        if (shallowCount >= 2) return true;

        // 3) 접속사만 있는 경우 방지(“그래서”만 있고 내용은 빈약한 케이스)
        return CONNECTORS.stream().anyMatch(text::contains) && !hasRealJustification;
    }

}

