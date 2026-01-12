package com.mockio.ai_service.fallback.question.marketing;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class MarketingFallbackQuestions {

    private MarketingFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<String>> byDifficulty() {
        Map<InterviewDifficulty, List<String>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                "마케팅의 목적은 무엇인가요?",
                "브랜딩과 퍼포먼스 마케팅의 차이는 무엇인가요?",
                "타겟 고객을 정의하는 방법은 무엇인가요?",
                "전환(Conversion)이란 무엇인가요?",
                "퍼널(Funnel)이란 무엇인가요?",
                "캠페인이란 무엇인가요?",
                "채널(검색/소셜/이메일 등)별 특징은 무엇인가요?",
                "콘텐츠 마케팅의 장점은 무엇인가요?",
                "CTR과 CVR의 의미는 무엇인가요?",
                "마케팅 성과를 측정해야 하는 이유는 무엇인가요?"
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                "마케팅 퍼널별 핵심 지표를 설명해 주세요.",
                "A/B 테스트를 마케팅 캠페인에 적용하는 방법은 무엇인가요?",
                "리타게팅/리마케팅의 원리와 주의사항은 무엇인가요?",
                "ROAS와 ROI의 차이를 설명해 주세요.",
                "캠페인 목표(인지/전환/리텐션)에 따른 전략 차이는 무엇인가요?",
                "랜딩페이지(LP) 전환율을 개선하는 방법은 무엇인가요?",
                "세그먼트별 메시지/크리에이티브를 최적화하는 방법은 무엇인가요?",
                "브랜드 가이드를 마케팅 실행에 어떻게 반영하나요?",
                "마케팅 믹스(4P/7P)를 활용하는 방법은 무엇인가요?",
                "유저 획득 비용(CAC)을 낮추는 방법은 무엇인가요?"
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                "채널 어트리뷰션(기여도)을 어떻게 설계하고 해석하나요?",
                "성과가 좋은데 브랜드가 약해지는 캠페인(단기 vs 장기)을 어떻게 균형 맞추나요?",
                "경쟁이 심한 키워드/시장 상황에서 차별화된 성장 전략은 무엇인가요?",
                "데이터 품질 문제(트래킹 누락 등)가 있을 때 성과 분석을 어떻게 보정하나요?",
                "실험 설계에서 통계적 유의성과 실무적 유의성을 어떻게 구분하나요?",
                "리텐션 마케팅(재방문/재구매) 전략을 설계하는 방법은 무엇인가요?",
                "가격/프로모션 전략이 장기 브랜드에 미치는 영향을 어떻게 관리하나요?",
                "바이럴/추천(Referral) 프로그램을 설계할 때 핵심 포인트는 무엇인가요?",
                "멀티 채널 캠페인에서 예산 배분을 최적화하는 방법은?",
                "마케팅 조직/프로세스를 스케일업할 때 발생하는 문제와 해결책은?"
        ));

        return Map.copyOf(map);
    }
}
