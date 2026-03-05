package com.mockio.ai_service.fallback.question.marketing;

import com.mockio.ai_service.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class MarketingFallbackQuestions {

    private MarketingFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<FallbackQuestion>> byDifficulty() {
        Map<InterviewDifficulty, List<FallbackQuestion>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                new FallbackQuestion(
                        "마케팅 목적",
                        "마케팅의 목적은 무엇인가요?",
                        List.of("Marketing", "Strategy", "Growth")
                ),
                new FallbackQuestion(
                        "브랜딩 vs 퍼포먼스",
                        "브랜딩과 퍼포먼스 마케팅의 차이는 무엇인가요?",
                        List.of("Branding", "PerformanceMarketing", "Strategy")
                ),
                new FallbackQuestion(
                        "타겟 고객 정의",
                        "타겟 고객을 정의하는 방법은 무엇인가요?",
                        List.of("Targeting", "Segmentation", "Customer")
                ),
                new FallbackQuestion(
                        "전환 개념",
                        "전환(Conversion)이란 무엇인가요?",
                        List.of("Conversion", "Funnel", "KPI")
                ),
                new FallbackQuestion(
                        "퍼널 개념",
                        "퍼널(Funnel)이란 무엇인가요?",
                        List.of("Funnel", "CustomerJourney", "Marketing")
                ),
                new FallbackQuestion(
                        "캠페인 정의",
                        "캠페인이란 무엇인가요?",
                        List.of("Campaign", "Promotion", "Marketing")
                ),
                new FallbackQuestion(
                        "채널 특징",
                        "채널(검색/소셜/이메일 등)별 특징은 무엇인가요?",
                        List.of("Channel", "DigitalMarketing", "Acquisition")
                ),
                new FallbackQuestion(
                        "콘텐츠 마케팅",
                        "콘텐츠 마케팅의 장점은 무엇인가요?",
                        List.of("ContentMarketing", "Brand", "Engagement")
                ),
                new FallbackQuestion(
                        "CTR vs CVR",
                        "CTR과 CVR의 의미는 무엇인가요?",
                        List.of("CTR", "CVR", "Metrics")
                ),
                new FallbackQuestion(
                        "성과 측정",
                        "마케팅 성과를 측정해야 하는 이유는 무엇인가요?",
                        List.of("Measurement", "ROI", "Analytics")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        "퍼널 지표",
                        "마케팅 퍼널별 핵심 지표를 설명해 주세요.",
                        List.of("Funnel", "Metrics", "KPI")
                ),
                new FallbackQuestion(
                        "A/B 테스트 적용",
                        "A/B 테스트를 마케팅 캠페인에 적용하는 방법은 무엇인가요?",
                        List.of("ABTest", "Experiment", "Optimization")
                ),
                new FallbackQuestion(
                        "리타게팅 전략",
                        "리타게팅/리마케팅의 원리와 주의사항은 무엇인가요?",
                        List.of("Retargeting", "Remarketing", "Ads")
                ),
                new FallbackQuestion(
                        "ROAS vs ROI",
                        "ROAS와 ROI의 차이를 설명해 주세요.",
                        List.of("ROAS", "ROI", "Performance")
                ),
                new FallbackQuestion(
                        "캠페인 목표 전략",
                        "캠페인 목표(인지/전환/리텐션)에 따른 전략 차이는 무엇인가요?",
                        List.of("Awareness", "Conversion", "Retention")
                ),
                new FallbackQuestion(
                        "LP 전환율 개선",
                        "랜딩페이지(LP) 전환율을 개선하는 방법은 무엇인가요?",
                        List.of("LandingPage", "ConversionRate", "UX")
                ),
                new FallbackQuestion(
                        "세그먼트 최적화",
                        "세그먼트별 메시지/크리에이티브를 최적화하는 방법은 무엇인가요?",
                        List.of("Segmentation", "Creative", "Personalization")
                ),
                new FallbackQuestion(
                        "브랜드 가이드 적용",
                        "브랜드 가이드를 마케팅 실행에 어떻게 반영하나요?",
                        List.of("BrandGuide", "Consistency", "Execution")
                ),
                new FallbackQuestion(
                        "마케팅 믹스 활용",
                        "마케팅 믹스(4P/7P)를 활용하는 방법은 무엇인가요?",
                        List.of("4P", "MarketingMix", "Strategy")
                ),
                new FallbackQuestion(
                        "CAC 절감",
                        "유저 획득 비용(CAC)을 낮추는 방법은 무엇인가요?",
                        List.of("CAC", "Acquisition", "Optimization")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        "어트리뷰션 설계",
                        "채널 어트리뷰션(기여도)을 어떻게 설계하고 해석하나요?",
                        List.of("Attribution", "Analytics", "Channel")
                ),
                new FallbackQuestion(
                        "단기 vs 장기 균형",
                        "성과가 좋은데 브랜드가 약해지는 캠페인(단기 vs 장기)을 어떻게 균형 맞추나요?",
                        List.of("Brand", "Performance", "Strategy")
                ),
                new FallbackQuestion(
                        "경쟁 시장 전략",
                        "경쟁이 심한 키워드/시장 상황에서 차별화된 성장 전략은 무엇인가요?",
                        List.of("Competition", "Growth", "Differentiation")
                ),
                new FallbackQuestion(
                        "데이터 보정",
                        "데이터 품질 문제(트래킹 누락 등)가 있을 때 성과 분석을 어떻게 보정하나요?",
                        List.of("Tracking", "DataQuality", "Analytics")
                ),
                new FallbackQuestion(
                        "통계적 유의성",
                        "실험 설계에서 통계적 유의성과 실무적 유의성을 어떻게 구분하나요?",
                        List.of("Statistics", "Experiment", "DecisionMaking")
                ),
                new FallbackQuestion(
                        "리텐션 전략",
                        "리텐션 마케팅(재방문/재구매) 전략을 설계하는 방법은 무엇인가요?",
                        List.of("Retention", "Lifecycle", "CRM")
                ),
                new FallbackQuestion(
                        "가격 전략 관리",
                        "가격/프로모션 전략이 장기 브랜드에 미치는 영향을 어떻게 관리하나요?",
                        List.of("Pricing", "Brand", "Strategy")
                ),
                new FallbackQuestion(
                        "Referral 프로그램",
                        "바이럴/추천(Referral) 프로그램을 설계할 때 핵심 포인트는 무엇인가요?",
                        List.of("Referral", "Viral", "Growth")
                ),
                new FallbackQuestion(
                        "예산 최적화",
                        "멀티 채널 캠페인에서 예산 배분을 최적화하는 방법은 무엇인가요?",
                        List.of("Budget", "Optimization", "Channel")
                ),
                new FallbackQuestion(
                        "마케팅 스케일업",
                        "마케팅 조직/프로세스를 스케일업할 때 발생하는 문제와 해결책은 무엇인가요?",
                        List.of("Scaling", "Process", "Growth")
                )
        ));

        return Map.copyOf(map);
    }

}
