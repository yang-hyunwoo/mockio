package com.mockio.core_service.ai.fallback.question.marketing;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.core_service.ai.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.generator.question.Question;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MarketingFallbackQuestions {

    private MarketingFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<FallbackQuestion>> byDifficulty() {
        Map<InterviewDifficulty, List<FallbackQuestion>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                new FallbackQuestion(
                        new Question(
                                "마케팅 목적",
                                "마케팅의 목적은 무엇인가요?"
                        ),
                        new Question(
                                "마케팅 목표 구조",
                                "마케팅의 목적을 브랜드 인지도, 유입, 전환 관점에서 어떻게 구조화할 수 있나요?"
                        ),
                        "Marketing",
                        Set.of("Marketing", "Strategy", "Growth")
                ),
                new FallbackQuestion(
                        new Question(
                                "브랜딩 vs 퍼포먼스",
                                "브랜딩과 퍼포먼스 마케팅의 차이는 무엇인가요?"
                        ),
                        new Question(
                                "전략 선택 기준",
                                "브랜딩과 퍼포먼스 마케팅을 어떤 상황에서 어떻게 병행해야 하나요?"
                        ),
                        "PerformanceMarketing",
                        Set.of("Branding", "PerformanceMarketing", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "타겟 고객 정의",
                                "타겟 고객을 정의하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "세그먼트 구체화",
                                "타겟 고객을 단순 인구통계가 아니라 행동 기반으로 정의하려면 어떻게 해야 하나요?"
                        ),
                        "Segmentation",
                        Set.of("Targeting", "Segmentation", "Customer")
                ),
                new FallbackQuestion(
                        new Question(
                                "전환 개념",
                                "전환(Conversion)이란 무엇인가요?"
                        ),
                        new Question(
                                "전환 설계",
                                "서비스에서 전환을 정의하고 최적화하기 위한 기준은 무엇인가요?"
                        ),
                        "Conversion",
                        Set.of("Conversion", "Funnel", "KPI")
                ),
                new FallbackQuestion(
                        new Question(
                                "퍼널 개념",
                                "퍼널(Funnel)이란 무엇인가요?"
                        ),
                        new Question(
                                "퍼널 개선 전략",
                                "각 퍼널 단계에서 이탈률을 줄이기 위해 어떤 개선 전략을 적용할 수 있나요?"
                        ),
                        "Funnel",
                        Set.of("Funnel", "CustomerJourney", "Marketing")
                ),
                new FallbackQuestion(
                        new Question(
                                "캠페인 정의",
                                "캠페인이란 무엇인가요?"
                        ),
                        new Question(
                                "캠페인 설계 요소",
                                "마케팅 캠페인을 설계할 때 목표, 메시지, 채널을 어떻게 구성해야 하나요?"
                        ),
                        "Campaign",
                        Set.of("Campaign", "Promotion", "Marketing")
                ),
                new FallbackQuestion(
                        new Question(
                                "채널 특징",
                                "채널(검색/소셜/이메일 등)별 특징은 무엇인가요?"
                        ),
                        new Question(
                                "채널 선택 기준",
                                "제품 특성과 타겟 고객에 따라 채널을 어떻게 선택해야 하나요?"
                        ),
                        "Channel",
                        Set.of("Channel", "DigitalMarketing", "Acquisition")
                ),
                new FallbackQuestion(
                        new Question(
                                "콘텐츠 마케팅",
                                "콘텐츠 마케팅의 장점은 무엇인가요?"
                        ),
                        new Question(
                                "콘텐츠 전략 설계",
                                "콘텐츠 마케팅을 통해 유입뿐 아니라 전환까지 이어지게 하려면 어떤 전략이 필요할까요?"
                        ),
                        "ContentMarketing",
                        Set.of("ContentMarketing", "Brand", "Engagement")
                ),
                new FallbackQuestion(
                        new Question(
                                "CTR vs CVR",
                                "CTR과 CVR의 의미는 무엇인가요?"
                        ),
                        new Question(
                                "지표 해석",
                                "CTR은 높은데 CVR이 낮은 경우 어떤 문제를 의심해야 하나요?"
                        ),
                        "CTR",
                        Set.of("CTR", "CVR", "Metrics")
                ),
                new FallbackQuestion(
                        new Question(
                                "성과 측정",
                                "마케팅 성과를 측정해야 하는 이유는 무엇인가요?"
                        ),
                        new Question(
                                "성과 지표 설계",
                                "마케팅 목표에 따라 KPI를 어떻게 다르게 설정해야 하나요?"
                        ),
                        "Measurement",
                        Set.of("Measurement", "ROI", "Analytics")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        new Question(
                                "퍼널 지표",
                                "마케팅 퍼널별 핵심 지표를 설명해 주세요."
                        ),
                        new Question(
                                "퍼널 병목 분석",
                                "퍼널 단계별 지표를 기반으로 병목 구간을 어떻게 찾아내고 개선하나요?"
                        ),
                        "Funnel",
                        Set.of("Funnel", "Metrics", "KPI")
                ),
                new FallbackQuestion(
                        new Question(
                                "A/B 테스트 적용",
                                "A/B 테스트를 마케팅 캠페인에 적용하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "실험 설계 기준",
                                "A/B 테스트에서 가설 설정과 결과 해석 시 주의해야 할 점은 무엇인가요?"
                        ),
                        "ABTest",
                        Set.of("ABTest", "Experiment", "Optimization")
                ),
                new FallbackQuestion(
                        new Question(
                                "리타게팅 전략",
                                "리타게팅/리마케팅의 원리와 주의사항은 무엇인가요?"
                        ),
                        new Question(
                                "리타게팅 리스크",
                                "리타게팅이 사용자 경험을 해칠 수 있는 상황과 이를 방지하는 방법은 무엇인가요?"
                        ),
                        "Retargeting",
                        Set.of("Retargeting", "Remarketing", "Ads")
                ),
                new FallbackQuestion(
                        new Question(
                                "ROAS vs ROI",
                                "ROAS와 ROI의 차이를 설명해 주세요."
                        ),
                        new Question(
                                "지표 선택 기준",
                                "ROAS가 높은데 ROI가 낮은 상황에서는 어떤 의사결정을 해야 하나요?"
                        ),
                        "ROAS",
                        Set.of("ROAS", "ROI", "Performance")
                ),
                new FallbackQuestion(
                        new Question(
                                "캠페인 목표 전략",
                                "캠페인 목표(인지/전환/리텐션)에 따른 전략 차이는 무엇인가요?"
                        ),
                        new Question(
                                "목표별 KPI 설계",
                                "인지, 전환, 리텐션 단계별로 어떤 KPI를 설정해야 하나요?"
                        ),
                        "Conversion",
                        Set.of("Awareness", "Conversion", "Retention")
                ),
                new FallbackQuestion(
                        new Question(
                                "LP 전환율 개선",
                                "랜딩페이지(LP) 전환율을 개선하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "전환 요소 최적화",
                                "CTA, 카피, 레이아웃을 활용해 전환율을 개선하는 방법을 설명해 주세요."
                        ),
                        "ConversionRate",
                        Set.of("LandingPage", "ConversionRate", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "세그먼트 최적화",
                                "세그먼트별 메시지/크리에이티브를 최적화하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "개인화 전략",
                                "세그먼트 기반 개인화 마케팅을 실행할 때 데이터 활용 방식은 어떻게 달라져야 하나요?"
                        ),
                        "Personalization",
                        Set.of("Segmentation", "Creative", "Personalization")
                ),
                new FallbackQuestion(
                        new Question(
                                "브랜드 가이드 적용",
                                "브랜드 가이드를 마케팅 실행에 어떻게 반영하나요?"
                        ),
                        new Question(
                                "브랜드 일관성 유지",
                                "다양한 채널에서 브랜드 일관성을 유지하기 위한 운영 전략은 무엇인가요?"
                        ),
                        "BrandGuide",
                        Set.of("BrandGuide", "Consistency", "Execution")
                ),
                new FallbackQuestion(
                        new Question(
                                "마케팅 믹스 활용",
                                "마케팅 믹스(4P/7P)를 활용하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "믹스 최적화",
                                "제품, 가격, 유통, 프로모션 요소를 상황에 맞게 어떻게 조정하시나요?"
                        ),
                        "4P",
                        Set.of("4P", "MarketingMix", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "CAC 절감",
                                "유저 획득 비용(CAC)을 낮추는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "CAC 구조 개선",
                                "CAC를 낮추기 위해 채널 믹스와 전환율을 어떻게 동시에 개선할 수 있나요?"
                        ),
                        "CAC",
                        Set.of("CAC", "Acquisition", "Optimization")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        new Question(
                                "어트리뷰션 설계",
                                "채널 어트리뷰션(기여도)을 어떻게 설계하고 해석하나요?"
                        ),
                        new Question(
                                "어트리뷰션 한계",
                                "멀티 터치 환경에서 어트리뷰션 모델이 가지는 한계와 보완 방법은 무엇인가요?"
                        ),
                        "Attribution",
                        Set.of("Attribution", "Analytics", "Channel")
                ),
                new FallbackQuestion(
                        new Question(
                                "단기 vs 장기 균형",
                                "성과가 좋은데 브랜드가 약해지는 캠페인(단기 vs 장기)을 어떻게 균형 맞추나요?"
                        ),
                        new Question(
                                "전략 우선순위 판단",
                                "단기 성과와 장기 브랜드 구축 사이에서 어떤 기준으로 의사결정을 내려야 하나요?"
                        ),
                        "Performance",
                        Set.of("Brand", "Performance", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "경쟁 시장 전략",
                                "경쟁이 심한 키워드/시장 상황에서 차별화된 성장 전략은 무엇인가요?"
                        ),
                        new Question(
                                "차별화 실행 전략",
                                "경쟁이 치열한 시장에서 가격, 메시지, 채널 전략을 어떻게 차별화하시겠습니까?"
                        ),
                        "Differentiation",
                        Set.of("Competition", "Growth", "Differentiation")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 보정",
                                "데이터 품질 문제(트래킹 누락 등)가 있을 때 성과 분석을 어떻게 보정하나요?"
                        ),
                        new Question(
                                "데이터 신뢰도 확보",
                                "트래킹 오류를 줄이고 데이터 신뢰도를 높이기 위한 시스템 설계는 어떻게 해야 하나요?"
                        ),
                        "Tracking",
                        Set.of("Tracking", "DataQuality", "Analytics")
                ),
                new FallbackQuestion(
                        new Question(
                                "통계적 유의성",
                                "실험 설계에서 통계적 유의성과 실무적 유의성을 어떻게 구분하나요?"
                        ),
                        new Question(
                                "실험 결과 해석",
                                "통계적으로 유의하지만 실무적으로 의미 없는 결과를 어떻게 판단해야 하나요?"
                        ),
                        "Statistics",
                        Set.of("Statistics", "Experiment", "DecisionMaking")
                ),
                new FallbackQuestion(
                        new Question(
                                "리텐션 전략",
                                "리텐션 마케팅(재방문/재구매) 전략을 설계하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "라이프사이클 마케팅",
                                "유저 생애주기(Lifecycle)에 맞춰 리텐션 전략을 어떻게 다르게 설계하시나요?"
                        ),
                        "Retention",
                        Set.of("Retention", "Lifecycle", "CRM")
                ),
                new FallbackQuestion(
                        new Question(
                                "가격 전략 관리",
                                "가격/프로모션 전략이 장기 브랜드에 미치는 영향을 어떻게 관리하나요?"
                        ),
                        new Question(
                                "가격 전략 리스크",
                                "지속적인 할인 전략이 브랜드에 미치는 부정적 영향을 어떻게 관리할 수 있나요?"
                        ),
                        "Pricing",
                        Set.of("Pricing", "Brand", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "Referral 프로그램",
                                "바이럴/추천(Referral) 프로그램을 설계할 때 핵심 포인트는 무엇인가요?"
                        ),
                        new Question(
                                "바이럴 확산 조건",
                                "Referral 프로그램이 자연스럽게 확산되기 위한 핵심 조건은 무엇인가요?"
                        ),
                        "Referral",
                        Set.of("Referral", "Viral", "Growth")
                ),
                new FallbackQuestion(
                        new Question(
                                "예산 최적화",
                                "멀티 채널 캠페인에서 예산 배분을 최적화하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "예산 배분 전략",
                                "성과 데이터를 기반으로 채널별 예산을 재분배하는 기준은 무엇인가요?"
                        ),
                        "Budget",
                        Set.of("Budget", "Optimization", "Channel")
                ),
                new FallbackQuestion(
                        new Question(
                                "마케팅 스케일업",
                                "마케팅 조직/프로세스를 스케일업할 때 발생하는 문제와 해결책은 무엇인가요?"
                        ),
                        new Question(
                                "스케일업 전략",
                                "마케팅 조직이 성장하면서 발생하는 비효율을 줄이기 위한 구조 개선 방법은 무엇인가요?"
                        ),
                        "Scaling",
                        Set.of("Scaling", "Process", "Growth")
                )
        ));

        return Map.copyOf(map);
    }
}