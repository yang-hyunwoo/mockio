package com.mockio.core_service.ai.fallback.question.business;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.core_service.ai.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.generator.question.Question;
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
                        new Question(
                                "사업 전략 개념",
                                "사업 전략이란 무엇인가요?"
                        ),
                        new Question(
                                "전략과 실행의 관계",
                                "사업 전략이 단순한 목표 설정이 아니라 실제 실행 방향과 연결되어야 하는 이유는 무엇인가요?"
                        ),
                        "Strategy",
                        Set.of("Strategy", "Business", "Planning")
                ),
                new FallbackQuestion(
                        new Question(
                                "시장 분석 중요성",
                                "시장 분석이 중요한 이유는 무엇인가요?"
                        ),
                        new Question(
                                "시장 분석 활용 방식",
                                "시장 분석 결과를 실제 사업 전략이나 제품 방향 결정에 어떻게 반영할 수 있나요?"
                        ),
                        "Analysis",
                        Set.of("Market", "Analysis", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "경쟁사 분석",
                                "경쟁사 분석은 어떻게 하나요?"
                        ),
                        new Question(
                                "경쟁사 분석의 실무 포인트",
                                "경쟁사 분석에서 단순 기능 비교를 넘어서 꼭 봐야 하는 요소는 무엇인가요?"
                        ),
                        "Competitor",
                        Set.of("Competitor", "Benchmark", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "수익 모델 종류",
                                "수익 모델의 종류에는 어떤 것들이 있나요?"
                        ),
                        new Question(
                                "수익 모델 선택 기준",
                                "구독형, 광고형, 거래 수수료형 모델 중 어떤 기준으로 적합한 수익 모델을 선택하시겠습니까?"
                        ),
                        "RevenueModel",
                        Set.of("RevenueModel", "Monetization", "Business")
                ),
                new FallbackQuestion(
                        new Question(
                                "손익 구조",
                                "손익(P&L)의 기본 구성요소는 무엇인가요?"
                        ),
                        new Question(
                                "손익 관점 의사결정",
                                "사업 의사결정에서 매출 성장 외에 비용 구조를 함께 봐야 하는 이유는 무엇인가요?"
                        ),
                        "PnL",
                        Set.of("PnL", "Finance", "Profit")
                ),
                new FallbackQuestion(
                        new Question(
                                "고객 세그먼트",
                                "고객 세그먼트란 무엇인가요?"
                        ),
                        new Question(
                                "세그먼트 구분 기준",
                                "고객 세그먼트를 나눌 때 인구통계, 행동, 니즈 기준을 어떻게 활용할 수 있나요?"
                        ),
                        "Segmentation",
                        Set.of("Customer", "Segmentation", "Target")
                ),
                new FallbackQuestion(
                        new Question(
                                "가치 제안",
                                "가치 제안(Value Proposition)이란 무엇인가요?"
                        ),
                        new Question(
                                "차별화된 가치 제안",
                                "고객이 우리 서비스를 선택해야 하는 이유를 가치 제안으로 정리할 때 어떤 요소를 포함해야 하나요?"
                        ),
                        "ValueProposition",
                        Set.of("ValueProposition", "Product", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "KPI 개념",
                                "KPI란 무엇인가요?"
                        ),
                        new Question(
                                "좋은 KPI 조건",
                                "좋은 KPI를 설계할 때 단순 수치가 아니라 실행 가능한 지표가 되려면 어떤 조건이 필요할까요?"
                        ),
                        "KPI",
                        Set.of("KPI", "Metric", "Performance")
                ),
                new FallbackQuestion(
                        new Question(
                                "전략 vs 프로젝트",
                                "프로젝트와 전략의 차이는 무엇인가요?"
                        ),
                        new Question(
                                "전략과 과제 정렬",
                                "개별 프로젝트가 전체 전략과 정렬되어 있는지 어떻게 판단할 수 있나요?"
                        ),
                        "Project",
                        Set.of("Strategy", "Project", "Execution")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 기반 의사결정",
                                "의사결정에 데이터가 중요한 이유는 무엇인가요?"
                        ),
                        new Question(
                                "데이터와 판단의 균형",
                                "데이터 기반 의사결정을 할 때 정량 데이터와 현장 감각 사이의 균형은 어떻게 잡아야 하나요?"
                        ),
                        "DecisionMaking",
                        Set.of("Data", "DecisionMaking", "Analytics")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        new Question(
                                "SWOT 활용",
                                "SWOT 분석을 실제 의사결정에 어떻게 연결하나요?"
                        ),
                        new Question(
                                "SWOT의 한계와 보완",
                                "SWOT 분석을 사용할 때 단순 나열로 끝나지 않게 하려면 어떤 방식으로 액션 아이템까지 연결해야 하나요?"
                        ),
                        "SWOT",
                        Set.of("SWOT", "Strategy", "DecisionMaking")
                ),
                new FallbackQuestion(
                        new Question(
                                "시장 규모 추정",
                                "시장 규모(TAM/SAM/SOM)를 추정하는 방법을 설명해 주세요."
                        ),
                        new Question(
                                "시장 규모 추정의 현실화",
                                "TAM, SAM, SOM을 계산할 때 지나치게 낙관적인 추정을 피하려면 어떤 검증 과정이 필요할까요?"
                        ),
                        "TAM",
                        Set.of("TAM", "MarketSizing", "Business")
                ),
                new FallbackQuestion(
                        new Question(
                                "사업 기회 평가",
                                "신규 사업 기회를 평가하는 프레임워크를 설명해 주세요."
                        ),
                        new Question(
                                "기회 평가 우선순위",
                                "여러 신규 사업 아이디어가 있을 때 시장성, 실행 가능성, 수익성 중 무엇을 우선 기준으로 볼지 설명해 주세요."
                        ),
                        "Framework",
                        Set.of("Opportunity", "Framework", "Evaluation")
                ),
                new FallbackQuestion(
                        new Question(
                                "가격 정책 설계",
                                "가격 정책을 설계할 때 고려해야 할 요소는 무엇인가요?"
                        ),
                        new Question(
                                "가격 결정의 실전 기준",
                                "원가, 경쟁사 가격, 고객 지불 의사 중 어떤 요소를 중심으로 가격 정책을 설계해야 하나요?"
                        ),
                        "Pricing",
                        Set.of("Pricing", "Revenue", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "유닛 이코노믹스",
                                "유닛 이코노믹스(Unit Economics)란 무엇이고 왜 중요한가요?"
                        ),
                        new Question(
                                "유닛 이코노믹스 악화 대응",
                                "고객은 늘어나는데 유닛 이코노믹스가 악화되는 상황에서는 어떤 지표를 먼저 점검해야 하나요?"
                        ),
                        "UnitEconomics",
                        Set.of("UnitEconomics", "LTV", "CAC")
                ),
                new FallbackQuestion(
                        new Question(
                                "CAC vs LTV",
                                "CAC와 LTV의 의미와 활용을 설명해 주세요."
                        ),
                        new Question(
                                "CAC/LTV 해석 기준",
                                "CAC 대비 LTV 비율을 해석할 때 업종별 차이와 회수 기간을 함께 봐야 하는 이유는 무엇인가요?"
                        ),
                        "CAC",
                        Set.of("CAC", "LTV", "Metrics")
                ),
                new FallbackQuestion(
                        new Question(
                                "파트너십 전략",
                                "파트너십/제휴 전략을 수립하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "파트너십 평가 기준",
                                "제휴 파트너를 선정할 때 단순 브랜드 인지도보다 더 중요하게 봐야 할 요소는 무엇인가요?"
                        ),
                        "Partnership",
                        Set.of("Partnership", "Alliance", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "OKR 운영",
                                "OKR을 수립하고 리뷰하는 프로세스를 설명해 주세요."
                        ),
                        new Question(
                                "OKR 실패 원인",
                                "OKR이 형식적으로만 운영되고 실제 성과 관리로 이어지지 않는 주요 원인은 무엇인가요?"
                        ),
                        "OKR",
                        Set.of("OKR", "GoalSetting", "Performance")
                ),
                new FallbackQuestion(
                        new Question(
                                "경쟁 우위 강화",
                                "경쟁 우위(모트)를 정의하고 강화하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "모트의 지속 가능성",
                                "한 번 만든 경쟁 우위가 시간이 지나도 유지되려면 어떤 구조적 조건이 필요할까요?"
                        ),
                        "Moat",
                        Set.of("Moat", "CompetitiveAdvantage", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "턴어라운드 전략",
                                "성과가 부진한 사업의 턴어라운드 접근법은 무엇인가요?"
                        ),
                        new Question(
                                "턴어라운드 우선순위",
                                "성과가 부진한 사업에서 매출 확대, 비용 절감, 상품 재정비 중 무엇부터 손대야 하는지 판단 기준을 설명해 주세요."
                        ),
                        "Turnaround",
                        Set.of("Turnaround", "Business", "Strategy")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        new Question(
                                "성장 vs 수익성",
                                "성장률은 높은데 수익성이 악화되는 상황에서 전략은 어떻게 조정하나요?"
                        ),
                        new Question(
                                "성장 전략 재설계",
                                "고성장을 유지하면서도 손익을 개선하려면 어떤 지표와 실행 과제를 중심으로 전략을 재설계해야 하나요?"
                        ),
                        "Profitability",
                        Set.of("Growth", "Profitability", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "규제 리스크 전략",
                                "규제/정책 변화가 큰 시장에서 리스크를 반영한 전략 수립 방법은 무엇인가요?"
                        ),
                        new Question(
                                "규제 불확실성 대응",
                                "규제 방향이 불확실한 시장에서 보수적 전략과 선제 투자 전략 중 어떻게 균형을 잡으시겠습니까?"
                        ),
                        "Regulation",
                        Set.of("Regulation", "Risk", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "자원 배분 최적화",
                                "멀티 프로덕트/멀티 마켓에서 자원 배분을 최적화하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "포트폴리오 자원 재배치",
                                "성과가 다른 여러 사업군 사이에서 인력과 예산을 재배치할 때 어떤 프레임워크를 활용하시겠습니까?"
                        ),
                        "ResourceAllocation",
                        Set.of("ResourceAllocation", "Portfolio", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "지표 트레이드오프",
                                "데이터가 상충할 때(지표 간 트레이드오프) 의사결정을 어떻게 하나요?"
                        ),
                        new Question(
                                "트레이드오프 우선순위",
                                "단기 매출과 장기 리텐션이 충돌할 때 어떤 기준으로 의사결정을 내려야 하나요?"
                        ),
                        "TradeOff",
                        Set.of("TradeOff", "Metrics", "DecisionMaking")
                ),
                new FallbackQuestion(
                        new Question(
                                "Go-To-Market 전략",
                                "신규 시장 진입(Go-to-Market) 전략을 단계별로 설명해 주세요."
                        ),
                        new Question(
                                "GTM 리스크 관리",
                                "신규 시장 진입 전략에서 채널, 가격, 메시지, 운영 리스크를 어떻게 검증하면서 확장하시겠습니까?"
                        ),
                        "GTM",
                        Set.of("GTM", "MarketEntry", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "M&A 검토",
                                "M&A/투자 검토 시 핵심 체크포인트(재무/시너지/리스크)는 무엇인가요?"
                        ),
                        new Question(
                                "M&A 의사결정 핵심",
                                "재무적으로 매력적이지만 전략적 시너지가 약한 딜을 어떻게 평가해야 하나요?"
                        ),
                        "M&A",
                        Set.of("M&A", "Finance", "DueDiligence")
                ),
                new FallbackQuestion(
                        new Question(
                                "네트워크 효과",
                                "플랫폼 비즈니스에서 네트워크 효과를 측정하고 강화하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "네트워크 효과 임계점",
                                "네트워크 효과가 실제로 작동하기 시작하는 임계점을 어떻게 판단하고 넘길 수 있을까요?"
                        ),
                        "NetworkEffect",
                        Set.of("Platform", "NetworkEffect", "Growth")
                ),
                new FallbackQuestion(
                        new Question(
                                "B2B vs B2C",
                                "B2B와 B2C 전략 수립의 차이를 설명해 주세요."
                        ),
                        new Question(
                                "시장별 전략 차별화",
                                "같은 제품이라도 B2B와 B2C 시장에서 가치 제안과 세일즈 전략이 어떻게 달라져야 하나요?"
                        ),
                        "B2B",
                        Set.of("B2B", "B2C", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "가격 실험 설계",
                                "가격 인상(또는 인하) 실험을 설계하고 리스크를 통제하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "가격 실험 검증 설계",
                                "가격 변화 실험에서 매출 상승만이 아니라 이탈률, 전환율, 고객 반응을 함께 봐야 하는 이유는 무엇인가요?"
                        ),
                        "Pricing",
                        Set.of("Pricing", "Experiment", "Risk")
                ),
                new FallbackQuestion(
                        new Question(
                                "전략 실행",
                                "조직 내 저항이 큰 전략 과제를 실행으로 옮기는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "변화관리 실행 방식",
                                "전략의 필요성에는 공감하지만 실행 저항이 큰 조직에서 변화관리를 어떻게 설계하시겠습니까?"
                        ),
                        "Execution",
                        Set.of("Execution", "ChangeManagement", "Leadership")
                )
        ));

        return Map.copyOf(map);
    }
}