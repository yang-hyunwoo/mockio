package com.mockio.ai_service.fallback.question.sales;

import com.mockio.ai_service.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SalesFallbackQuestions {

    private SalesFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<FallbackQuestion>> byDifficulty() {
        Map<InterviewDifficulty, List<FallbackQuestion>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                new FallbackQuestion(
                        "영업 역할",
                        "영업의 역할은 무엇인가요?",
                        Set.of("Sales", "Role", "Revenue")
                ),
                new FallbackQuestion(
                        "리드 정의",
                        "리드(Lead)란 무엇인가요?",
                        Set.of("Lead", "Prospect", "Pipeline")
                ),
                new FallbackQuestion(
                        "B2B vs B2C 영업",
                        "B2B와 B2C 영업의 차이는 무엇인가요?",
                        Set.of("B2B", "B2C", "SalesStrategy")
                ),
                new FallbackQuestion(
                        "파이프라인 개념",
                        "파이프라인이란 무엇인가요?",
                        Set.of("Pipeline", "Forecast", "SalesProcess")
                ),
                new FallbackQuestion(
                        "미팅 준비 요소",
                        "미팅 준비에서 중요한 요소는 무엇인가요?",
                        Set.of("Preparation", "CustomerResearch", "Sales")
                ),
                new FallbackQuestion(
                        "고객 니즈 파악",
                        "고객 니즈를 파악하는 방법은 무엇인가요?",
                        Set.of("NeedsAnalysis", "Discovery", "ConsultativeSelling")
                ),
                new FallbackQuestion(
                        "제안서 구성",
                        "제안서의 기본 구성은 무엇인가요?",
                        Set.of("Proposal", "ValueProposition", "Sales")
                ),
                new FallbackQuestion(
                        "클로징 개념",
                        "클로징(Closing)이란 무엇인가요?",
                        Set.of("Closing", "Negotiation", "Revenue")
                ),
                new FallbackQuestion(
                        "CRM 필요성",
                        "CRM은 왜 필요한가요?",
                        Set.of("CRM", "CustomerManagement", "Data")
                ),
                new FallbackQuestion(
                        "관계 구축",
                        "관계 구축이 중요한 이유는 무엇인가요?",
                        Set.of("Relationship", "Trust", "Retention")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        "파이프라인 관리",
                        "영업 파이프라인 단계별 관리 방법을 설명해 주세요.",
                        Set.of("Pipeline", "StageManagement", "Forecast")
                ),
                new FallbackQuestion(
                        "반대 의견 처리",
                        "반대 의견(Objection)을 처리하는 접근법은 무엇인가요?",
                        Set.of("ObjectionHandling", "Negotiation", "Sales")
                ),
                new FallbackQuestion(
                        "가격 협상 전략",
                        "가격 협상 시 핵심 전략과 주의점은 무엇인가요?",
                        Set.of("Pricing", "Negotiation", "ValueBasedSelling")
                ),
                new FallbackQuestion(
                        "의사결정 구조 파악",
                        "고객의 의사결정 구조(Decision Maker)를 파악하는 방법은 무엇인가요?",
                        Set.of("DecisionMaker", "Stakeholder", "EnterpriseSales")
                ),
                new FallbackQuestion(
                        "세일즈 사이클 단축",
                        "세일즈 사이클을 단축하는 방법은 무엇인가요?",
                        Set.of("SalesCycle", "Efficiency", "Closing")
                ),
                new FallbackQuestion(
                        "리드 퀄리피케이션",
                        "리드 퀄리피케이션(BANT 등) 기준을 설명해 주세요.",
                        Set.of("BANT", "Qualification", "LeadScoring")
                ),
                new FallbackQuestion(
                        "경쟁사 설득",
                        "경쟁사 대비 차별점을 설득하는 방법은 무엇인가요?",
                        Set.of("Differentiation", "CompetitiveSales", "Value")
                ),
                new FallbackQuestion(
                        "업셀/크로스셀",
                        "업셀/크로스셀을 성공시키는 조건은 무엇인가요?",
                        Set.of("Upsell", "CrossSell", "Expansion")
                ),
                new FallbackQuestion(
                        "세일즈 포캐스트",
                        "세일즈 포캐스트(예측)를 어떻게 만들고 관리하나요?",
                        Set.of("Forecast", "Revenue", "DataDriven")
                ),
                new FallbackQuestion(
                        "계약/법무 협업",
                        "계약/법무 이슈가 생길 때 협업 방식은 어떻게 하나요?",
                        Set.of("Legal", "Collaboration", "RiskManagement")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        "엔터프라이즈 딜 전략",
                        "대형 딜(엔터프라이즈)에서 이해관계자 지형을 공략하는 전략은 무엇인가요?",
                        Set.of("EnterpriseSales", "StakeholderMapping", "Strategy")
                ),
                new FallbackQuestion(
                        "목표 vs 관계",
                        "매출 목표와 장기 고객 관계가 충돌할 때 어떻게 의사결정하나요?",
                        Set.of("TradeOff", "CustomerRelationship", "Strategy")
                ),
                new FallbackQuestion(
                        "가치 기반 협상",
                        "가격 인하 요구가 강할 때, 가치 기반(Value-based) 협상을 어떻게 하나요?",
                        Set.of("ValueBasedSelling", "Negotiation", "Pricing")
                ),
                new FallbackQuestion(
                        "리뉴얼 리스크 감지",
                        "갱신/리뉴얼(renewal) 리스크를 조기 감지하는 지표와 방법은 무엇인가요?",
                        Set.of("Renewal", "ChurnRisk", "CustomerSuccess")
                ),
                new FallbackQuestion(
                        "부서 간 충돌 조율",
                        "세일즈와 CS/프로덕트가 충돌할 때 조율하는 방법은 무엇인가요?",
                        Set.of("CrossFunctional", "Alignment", "ConflictManagement")
                ),
                new FallbackQuestion(
                        "클로징 부진 진단",
                        "파이프라인은 많지만 클로징이 안 될 때 진단 프레임워크는 무엇인가요?",
                        Set.of("PipelineAnalysis", "ConversionRate", "Diagnosis")
                ),
                new FallbackQuestion(
                        "글로벌 영업 관리",
                        "복수 국가/리전 고객 영업에서 프로세스/리스크를 어떻게 관리하나요?",
                        Set.of("GlobalSales", "Compliance", "Process")
                ),
                new FallbackQuestion(
                        "RFP 대응 전략",
                        "대규모 RFP 대응 전략을 단계별로 설명해 주세요.",
                        Set.of("RFP", "Enterprise", "BidStrategy")
                ),
                new FallbackQuestion(
                        "세일즈 스케일업",
                        "세일즈 조직을 스케일업할 때 KPI와 코칭 체계를 어떻게 설계하나요?",
                        Set.of("SalesScaling", "KPI", "Coaching")
                ),
                new FallbackQuestion(
                        "Lost Deal 분석",
                        "손실 딜(Lost deal) 분석을 통해 프로세스를 개선하는 방법은 무엇인가요?",
                        Set.of("LostDeal", "PostMortem", "Improvement")
                )
        ));

        return Map.copyOf(map);
    }

}
