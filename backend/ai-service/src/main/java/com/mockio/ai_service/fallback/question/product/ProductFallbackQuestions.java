package com.mockio.ai_service.fallback.question.product;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class ProductFallbackQuestions {

    private ProductFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<String>> byDifficulty() {
        Map<InterviewDifficulty, List<String>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                "PM의 역할은 무엇인가요?",
                "기획서(PRD)란 무엇인가요?",
                "요구사항과 기능 정의의 차이는 무엇인가요?",
                "우선순위를 정할 때 어떤 기준을 쓰나요?",
                "MVP란 무엇인가요?",
                "사용자 페르소나란 무엇인가요?",
                "유저 스토리는 왜 작성하나요?",
                "릴리즈 노트의 목적은 무엇인가요?",
                "커뮤니케이션이 중요한 이유는 무엇인가요?",
                "성공적인 제품의 기준은 무엇이라고 생각하나요?"
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                "제품 로드맵을 수립하는 방법을 설명해 주세요.",
                "KPI/OKR을 설정하고 관리하는 방법은 무엇인가요?",
                "A/B 테스트를 설계할 때 고려해야 할 사항은 무엇인가요?",
                "요구사항 변경(스코프 크립)을 어떻게 관리하나요?",
                "이해관계자(개발/디자인/비즈니스) 조율은 어떻게 하나요?",
                "사용자 리서치를 통해 인사이트를 도출하는 방법은 무엇인가요?",
                "데이터 기반 의사결정과 직관 기반 의사결정의 균형은 어떻게 맞추나요?",
                "제품 품질(버그/안정성)과 일정 사이의 트레이드오프를 어떻게 다루나요?",
                "제품 지표(전환, 리텐션 등)를 모니터링하고 개선하는 접근은 무엇인가요?",
                "경쟁사 분석을 제품 전략에 어떻게 반영하나요?"
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                "전사 목표와 제품 로드맵을 정렬(Alignment)시키는 방법은 무엇인가요?",
                "지표가 좋아 보이는데 실제 제품 가치는 악화되는 사례를 어떻게 방지하나요?",
                "실험(Experimentation) 문화가 없는 조직에서 A/B 테스트를 정착시키는 방법은?",
                "매출과 사용자 경험이 충돌할 때 의사결정을 어떻게 하나요?",
                "대규모 제품에서 권한/책임(R&R) 경계가 불명확할 때 해결 방법은?",
                "제품의 북극성 지표(North Star Metric)를 정의하는 방법을 설명해 주세요.",
                "리텐션이 급감했을 때 원인 분석과 대응 절차를 설명해 주세요.",
                "다국가/다언어 제품 확장 시 주요 리스크와 대응은 무엇인가요?",
                "기술 부채가 제품 속도를 저해할 때 PM으로서 어떻게 우선순위를 조정하나요?",
                "제품 실패를 빠르게 감지하고 피벗하는 신호와 방법은 무엇인가요?"
        ));

        return Map.copyOf(map);
    }

}
