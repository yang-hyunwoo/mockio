package com.mockio.ai_service.fallback.question.design;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class DesignFallbackQuestions {

    private DesignFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<String>> byDifficulty() {
        Map<InterviewDifficulty, List<String>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                "그래픽 디자이너의 역할은 무엇인가요?",
                "UI와 UX의 차이는 무엇인가요?",
                "컬러 이론이 중요한 이유는 무엇인가요?",
                "타이포그래피란 무엇인가요?",
                "디자인 가이드라인은 왜 필요한가요?",
                "웹과 모바일 디자인의 차이는 무엇인가요?",
                "아이콘 디자인의 중요성은 무엇인가요?",
                "와이어프레임이란 무엇인가요?",
                "사용자 관점 디자인이 중요한 이유는 무엇인가요?",
                "디자인 피드백을 어떻게 수용하나요?"
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                "디자인 시스템이 필요한 이유는 무엇인가요?",
                "접근성(Accessibility)을 고려한 디자인이란 무엇인가요?",
                "UX 리서치는 어떻게 진행하나요?",
                "반응형 디자인의 핵심 포인트는 무엇인가요?",
                "프로토타입을 사용하는 이유는 무엇인가요?",
                "브랜드 아이덴티티를 디자인에 어떻게 반영하나요?",
                "사용성 테스트는 언제 필요한가요?",
                "디자인 일관성을 유지하는 방법은 무엇인가요?",
                "협업 시 개발자와의 커뮤니케이션 방법은?",
                "디자인 툴 선택 기준은 무엇인가요?"
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                "대규모 서비스에서 디자인 시스템을 확장하는 방법은?",
                "데이터 기반 UX 개선 사례를 설명해 주세요.",
                "글로벌 서비스를 위한 디자인 고려사항은?",
                "접근성과 심미성을 동시에 만족시키는 방법은?",
                "디자인 변경이 KPI에 미치는 영향을 어떻게 측정하나요?",
                "복잡한 사용자 플로우를 단순화하는 전략은?",
                "디자인 기술 부채를 관리하는 방법은?",
                "A/B 테스트를 디자인에 어떻게 활용하나요?",
                "브랜드 리뉴얼 시 주요 리스크는 무엇인가요?",
                "디자인 의사결정을 설득하는 방법은 무엇인가요?"
        ));

        return Map.copyOf(map);
    }
}
