package com.mockio.core_service.ai.fallback.question.sales;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.core_service.ai.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.generator.question.Question;

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
                        new Question("영업 역할",
                                "영업의 역할은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Sales",
                        Set.of("Sales", "Role", "Revenue")
                ),
                new FallbackQuestion(
                        new Question("리드 정의",
                                "리드(Lead)란 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Lead",
                        Set.of("Lead", "Prospect", "Pipeline")
                ),
                new FallbackQuestion(
                        new Question("B2B vs B2C 영업",
                                "B2B와 B2C 영업의 차이는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "B2B",
                        Set.of("B2B", "B2C", "SalesStrategy")
                ),
                new FallbackQuestion(
                        new Question("파이프라인 개념",
                                "파이프라인이란 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Pipeline",
                        Set.of("Pipeline", "Forecast", "SalesProcess")
                ),
                new FallbackQuestion(
                        new Question("미팅 준비 요소",
                                "미팅 준비에서 중요한 요소는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Preparation",
                        Set.of("Preparation", "CustomerResearch", "Sales")
                ),
                new FallbackQuestion(
                        new Question("고객 니즈 파악",
                                "고객 니즈를 파악하는 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "NeedsAnalysis",
                        Set.of("NeedsAnalysis", "Discovery", "ConsultativeSelling")
                ),
                new FallbackQuestion(
                        new Question("제안서 구성",
                                "제안서의 기본 구성은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Proposal",
                        Set.of("Proposal", "ValueProposition", "Sales")
                ),
                new FallbackQuestion(
                        new Question("클로징 개념",
                                "클로징(Closing)이란 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Closing",
                        Set.of("Closing", "Negotiation", "Revenue")
                ),
                new FallbackQuestion(
                        new Question("CRM 필요성",
                                "CRM은 왜 필요한가요?",
                                "BASIC",
                                null
                        ),
                        "CRM",
                        Set.of("CRM", "CustomerManagement", "Data")
                ),
                new FallbackQuestion(
                        new Question("관계 구축",
                                "관계 구축이 중요한 이유는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Relationship",
                        Set.of("Relationship", "Trust", "Retention")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        new Question("파이프라인 관리",
                                "영업 파이프라인 단계별 관리 방법을 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "StageManagement",
                        Set.of("Pipeline", "StageManagement", "Forecast")
                ),
                new FallbackQuestion(
                        new Question("반대 의견 처리",
                                "반대 의견을 처리하는 접근법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "ObjectionHandling",
                        Set.of("ObjectionHandling", "Negotiation", "Sales")
                ),
                new FallbackQuestion(
                        new Question("가격 협상 전략",
                                "가격 협상 시 핵심 전략은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "ValueBasedSelling",
                        Set.of("Pricing", "Negotiation", "ValueBasedSelling")
                ),
                new FallbackQuestion(
                        new Question("의사결정 구조 파악",
                                "고객의 의사결정 구조를 파악하는 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "DecisionMaker",
                        Set.of("DecisionMaker", "Stakeholder", "EnterpriseSales")
                ),
                new FallbackQuestion(
                        new Question("세일즈 사이클 단축",
                                "세일즈 사이클을 단축하는 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "SalesCycle",
                        Set.of("SalesCycle", "Efficiency", "Closing")
                ),
                new FallbackQuestion(
                        new Question("리드 퀄리피케이션",
                                "리드 퀄리피케이션 기준을 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "BANT",
                        Set.of("BANT", "Qualification", "LeadScoring")
                ),
                new FallbackQuestion(
                        new Question("경쟁사 설득",
                                "경쟁사 대비 차별점을 설득하는 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Differentiation",
                        Set.of("Differentiation", "CompetitiveSales", "Value")
                ),
                new FallbackQuestion(
                        new Question("업셀/크로스셀",
                                "업셀/크로스셀을 성공시키는 조건은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Upsell",
                        Set.of("Upsell", "CrossSell", "Expansion")
                ),
                new FallbackQuestion(
                        new Question("세일즈 포캐스트",
                                "세일즈 포캐스트를 어떻게 만들고 관리하나요?",
                                "BASIC",
                                null
                        ),
                        "Forecast",
                        Set.of("Forecast", "Revenue", "DataDriven")
                ),
                new FallbackQuestion(
                        new Question("계약/법무 협업",
                                "계약/법무 이슈 발생 시 협업 방식은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Legal",
                        Set.of("Legal", "Collaboration", "RiskManagement")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        new Question("엔터프라이즈 딜 전략",
                                "대형 딜에서 이해관계자를 공략하는 전략은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "EnterpriseSales",
                        Set.of("EnterpriseSales", "StakeholderMapping", "Strategy")
                ),
                new FallbackQuestion(
                        new Question("목표 vs 관계",
                                "매출 목표와 장기 고객 관계가 충돌할 때 어떻게 의사결정하나요?",
                                "BASIC",
                                null
                        ),
                        "TradeOff",
                        Set.of("TradeOff", "CustomerRelationship", "Strategy")
                ),
                new FallbackQuestion(
                        new Question("가치 기반 협상",
                                "가격 인하 요구 상황에서 협상 전략은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "ValueBasedSelling",
                        Set.of("ValueBasedSelling", "Negotiation", "Pricing")
                ),
                new FallbackQuestion(
                        new Question("리뉴얼 리스크 감지",
                                "리뉴얼 리스크를 조기에 감지하는 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Renewal",
                        Set.of("Renewal", "ChurnRisk", "CustomerSuccess")
                ),
                new FallbackQuestion(
                        new Question("부서 간 충돌 조율",
                                "세일즈와 타 부서 충돌 시 해결 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "CrossFunctional",
                        Set.of("CrossFunctional", "Alignment", "ConflictManagement")
                ),
                new FallbackQuestion(
                        new Question("클로징 부진 진단",
                                "클로징이 안 되는 원인을 어떻게 분석하나요?",
                                "BASIC",
                                null
                        ),
                        "PipelineAnalysis",
                        Set.of("PipelineAnalysis", "ConversionRate", "Diagnosis")
                ),
                new FallbackQuestion(
                        new Question("글로벌 영업 관리",
                                "글로벌 영업에서 리스크를 어떻게 관리하나요?",
                                "BASIC",
                                null
                        ),
                        "GlobalSales",
                        Set.of("GlobalSales", "Compliance", "Process")
                ),
                new FallbackQuestion(
                        new Question("RFP 대응 전략",
                                "대규모 RFP 대응 전략은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "RFP",
                        Set.of("RFP", "Enterprise", "BidStrategy")
                ),
                new FallbackQuestion(
                        new Question("세일즈 스케일업",
                                "세일즈 조직을 스케일업하는 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "SalesScaling",
                        Set.of("SalesScaling", "KPI", "Coaching")
                ),
                new FallbackQuestion(
                        new Question("Lost Deal 분석",
                                "Lost deal을 분석하는 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "LostDeal",
                        Set.of("LostDeal", "PostMortem", "Improvement")
                )
        ));

        return Map.copyOf(map);
    }
}