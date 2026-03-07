package com.mockio.ai_service.fallback.question.business;


import com.mockio.ai_service.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BusinessFallbackQuestions {

    private BusinessFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<FallbackQuestion>> byDifficulty() {
        Map<InterviewDifficulty, List<FallbackQuestion>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                new FallbackQuestion(
                        "사업 전략 개념",
                        "사업 전략이란 무엇인가요?",
                        Set.of("Strategy", "Business", "Planning")
                ),
                new FallbackQuestion(
                        "시장 분석 중요성",
                        "시장 분석이 중요한 이유는 무엇인가요?",
                        Set.of("Market", "Analysis", "Strategy")
                ),
                new FallbackQuestion(
                        "경쟁사 분석",
                        "경쟁사 분석은 어떻게 하나요?",
                        Set.of("Competitor", "Benchmark", "Strategy")
                ),
                new FallbackQuestion(
                        "수익 모델 종류",
                        "수익 모델의 종류에는 어떤 것들이 있나요?",
                        Set.of("RevenueModel", "Monetization", "Business")
                ),
                new FallbackQuestion(
                        "손익 구조",
                        "손익(P&L)의 기본 구성요소는 무엇인가요?",
                        Set.of("PnL", "Finance", "Profit")
                ),
                new FallbackQuestion(
                        "고객 세그먼트",
                        "고객 세그먼트란 무엇인가요?",
                        Set.of("Customer", "Segmentation", "Target")
                ),
                new FallbackQuestion(
                        "가치 제안",
                        "가치 제안(Value Proposition)이란 무엇인가요?",
                        Set.of("ValueProposition", "Product", "Strategy")
                ),
                new FallbackQuestion(
                        "KPI 개념",
                        "KPI란 무엇인가요?",
                        Set.of("KPI", "Metric", "Performance")
                ),
                new FallbackQuestion(
                        "전략 vs 프로젝트",
                        "프로젝트와 전략의 차이는 무엇인가요?",
                        Set.of("Strategy", "Project", "Execution")
                ),
                new FallbackQuestion(
                        "데이터 기반 의사결정",
                        "의사결정에 데이터가 중요한 이유는 무엇인가요?",
                        Set.of("Data", "DecisionMaking", "Analytics")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        "SWOT 활용",
                        "SWOT 분석을 실제 의사결정에 어떻게 연결하나요?",
                        Set.of("SWOT", "Strategy", "DecisionMaking")
                ),
                new FallbackQuestion(
                        "시장 규모 추정",
                        "시장 규모(TAM/SAM/SOM)를 추정하는 방법을 설명해 주세요.",
                        Set.of("TAM", "MarketSizing", "Business")
                ),
                new FallbackQuestion(
                        "사업 기회 평가",
                        "신규 사업 기회를 평가하는 프레임워크를 설명해 주세요.",
                        Set.of("Opportunity", "Framework", "Evaluation")
                ),
                new FallbackQuestion(
                        "가격 정책 설계",
                        "가격 정책을 설계할 때 고려해야 할 요소는 무엇인가요?",
                        Set.of("Pricing", "Revenue", "Strategy")
                ),
                new FallbackQuestion(
                        "유닛 이코노믹스",
                        "유닛 이코노믹스(Unit Economics)란 무엇이고 왜 중요한가요?",
                        Set.of("UnitEconomics", "LTV", "CAC")
                ),
                new FallbackQuestion(
                        "CAC vs LTV",
                        "CAC와 LTV의 의미와 활용을 설명해 주세요.",
                        Set.of("CAC", "LTV", "Metrics")
                ),
                new FallbackQuestion(
                        "파트너십 전략",
                        "파트너십/제휴 전략을 수립하는 방법은 무엇인가요?",
                        Set.of("Partnership", "Alliance", "Strategy")
                ),
                new FallbackQuestion(
                        "OKR 운영",
                        "OKR을 수립하고 리뷰하는 프로세스를 설명해 주세요.",
                        Set.of("OKR", "GoalSetting", "Performance")
                ),
                new FallbackQuestion(
                        "경쟁 우위 강화",
                        "경쟁 우위(모트)를 정의하고 강화하는 방법은 무엇인가요?",
                        Set.of("Moat", "CompetitiveAdvantage", "Strategy")
                ),
                new FallbackQuestion(
                        "턴어라운드 전략",
                        "성과가 부진한 사업의 턴어라운드 접근법은 무엇인가요?",
                        Set.of("Turnaround", "Business", "Strategy")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        "성장 vs 수익성",
                        "성장률은 높은데 수익성이 악화되는 상황에서 전략은 어떻게 조정하나요?",
                        Set.of("Growth", "Profitability", "Strategy")
                ),
                new FallbackQuestion(
                        "규제 리스크 전략",
                        "규제/정책 변화가 큰 시장에서 리스크를 반영한 전략 수립 방법은 무엇인가요?",
                        Set.of("Regulation", "Risk", "Strategy")
                ),
                new FallbackQuestion(
                        "자원 배분 최적화",
                        "멀티 프로덕트/멀티 마켓에서 자원 배분을 최적화하는 방법은 무엇인가요?",
                        Set.of("ResourceAllocation", "Portfolio", "Strategy")
                ),
                new FallbackQuestion(
                        "지표 트레이드오프",
                        "데이터가 상충할 때(지표 간 트레이드오프) 의사결정을 어떻게 하나요?",
                        Set.of("TradeOff", "Metrics", "DecisionMaking")
                ),
                new FallbackQuestion(
                        "Go-To-Market 전략",
                        "신규 시장 진입(Go-to-Market) 전략을 단계별로 설명해 주세요.",
                        Set.of("GTM", "MarketEntry", "Strategy")
                ),
                new FallbackQuestion(
                        "M&A 검토",
                        "M&A/투자 검토 시 핵심 체크포인트(재무/시너지/리스크)는 무엇인가요?",
                        Set.of("M&A", "Finance", "DueDiligence")
                ),
                new FallbackQuestion(
                        "네트워크 효과",
                        "플랫폼 비즈니스에서 네트워크 효과를 측정하고 강화하는 방법은 무엇인가요?",
                        Set.of("Platform", "NetworkEffect", "Growth")
                ),
                new FallbackQuestion(
                        "B2B vs B2C",
                        "B2B와 B2C 전략 수립의 차이를 설명해 주세요.",
                        Set.of("B2B", "B2C", "Strategy")
                ),
                new FallbackQuestion(
                        "가격 실험 설계",
                        "가격 인상(또는 인하) 실험을 설계하고 리스크를 통제하는 방법은 무엇인가요?",
                        Set.of("Pricing", "Experiment", "Risk")
                ),
                new FallbackQuestion(
                        "전략 실행",
                        "조직 내 저항이 큰 전략 과제를 실행으로 옮기는 방법은 무엇인가요?",
                        Set.of("Execution", "ChangeManagement", "Leadership")
                )
        ));

        return Map.copyOf(map);
    }

}
