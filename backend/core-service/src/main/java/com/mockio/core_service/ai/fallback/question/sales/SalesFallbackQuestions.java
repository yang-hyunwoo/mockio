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
                        new Question("영업 역할", "영업의 역할은 무엇인가요?"),
                        new Question("영업 역할 확장", "영업이 단순 판매를 넘어 고객 가치 창출과 관계 구축에 기여하는 방식은 무엇인가요?"),
                        "Sales",
                        Set.of("Sales", "Role", "Revenue")
                ),
                new FallbackQuestion(
                        new Question("리드 정의", "리드(Lead)란 무엇인가요?"),
                        new Question("리드 관리 기준", "리드를 Prospect와 Opportunity로 구분하는 기준은 무엇인가요?"),
                        "Lead",
                        Set.of("Lead", "Prospect", "Pipeline")
                ),
                new FallbackQuestion(
                        new Question("B2B vs B2C 영업", "B2B와 B2C 영업의 차이는 무엇인가요?"),
                        new Question("영업 전략 차이", "B2B와 B2C에서 세일즈 사이클과 의사결정 구조는 어떻게 달라지나요?"),
                        "B2B",
                        Set.of("B2B", "B2C", "SalesStrategy")
                ),
                new FallbackQuestion(
                        new Question("파이프라인 개념", "파이프라인이란 무엇인가요?"),
                        new Question("파이프라인 관리", "파이프라인 단계별 전환율을 관리하는 방법은 무엇인가요?"),
                        "Pipeline",
                        Set.of("Pipeline", "Forecast", "SalesProcess")
                ),
                new FallbackQuestion(
                        new Question("미팅 준비 요소", "미팅 준비에서 중요한 요소는 무엇인가요?"),
                        new Question("미팅 전략 설계", "고객 미팅 전에 어떤 정보를 분석하고 전략을 준비해야 하나요?"),
                        "Preparation",
                        Set.of("Preparation", "CustomerResearch", "Sales")
                ),
                new FallbackQuestion(
                        new Question("고객 니즈 파악", "고객 니즈를 파악하는 방법은 무엇인가요?"),
                        new Question("니즈 분석 심화", "표면적인 요구와 실제 비즈니스 문제를 구분하는 방법은 무엇인가요?"),
                        "NeedsAnalysis",
                        Set.of("NeedsAnalysis", "Discovery", "ConsultativeSelling")
                ),
                new FallbackQuestion(
                        new Question("제안서 구성", "제안서의 기본 구성은 무엇인가요?"),
                        new Question("제안서 차별화", "고객 상황에 맞춘 제안서를 작성하기 위한 핵심 요소는 무엇인가요?"),
                        "Proposal",
                        Set.of("Proposal", "ValueProposition", "Sales")
                ),
                new FallbackQuestion(
                        new Question("클로징 개념", "클로징(Closing)이란 무엇인가요?"),
                        new Question("클로징 전략", "클로징을 성공시키기 위한 타이밍과 전략은 무엇인가요?"),
                        "Closing",
                        Set.of("Closing", "Negotiation", "Revenue")
                ),
                new FallbackQuestion(
                        new Question("CRM 필요성", "CRM은 왜 필요한가요?"),
                        new Question("CRM 활용 전략", "CRM 데이터를 활용해 영업 성과를 개선하는 방법은 무엇인가요?"),
                        "CRM",
                        Set.of("CRM", "CustomerManagement", "Data")
                ),
                new FallbackQuestion(
                        new Question("관계 구축", "관계 구축이 중요한 이유는 무엇인가요?"),
                        new Question("장기 관계 전략", "장기 고객 관계를 유지하기 위한 구체적인 전략은 무엇인가요?"),
                        "Relationship",
                        Set.of("Relationship", "Trust", "Retention")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        new Question("파이프라인 관리", "영업 파이프라인 단계별 관리 방법을 설명해 주세요."),
                        new Question("전환율 개선", "각 단계의 전환율을 높이기 위한 전략은 무엇인가요?"),
                        "StageManagement",
                        Set.of("Pipeline", "StageManagement", "Forecast")
                ),
                new FallbackQuestion(
                        new Question("반대 의견 처리", "반대 의견을 처리하는 접근법은 무엇인가요?"),
                        new Question("설득 전략", "고객의 반대 의견을 기회로 전환하는 방법은 무엇인가요?"),
                        "ObjectionHandling",
                        Set.of("ObjectionHandling", "Negotiation", "Sales")
                ),
                new FallbackQuestion(
                        new Question("가격 협상 전략", "가격 협상 시 핵심 전략은 무엇인가요?"),
                        new Question("가치 기반 협상", "가격이 아니라 가치를 중심으로 협상하는 방법은 무엇인가요?"),
                        "ValueBasedSelling",
                        Set.of("Pricing", "Negotiation", "ValueBasedSelling")
                ),
                new FallbackQuestion(
                        new Question("의사결정 구조 파악", "고객의 의사결정 구조를 파악하는 방법은 무엇인가요?"),
                        new Question("의사결정 영향력", "Decision Maker와 Influencer를 구분하고 접근하는 방법은 무엇인가요?"),
                        "DecisionMaker",
                        Set.of("DecisionMaker", "Stakeholder", "EnterpriseSales")
                ),
                new FallbackQuestion(
                        new Question("세일즈 사이클 단축", "세일즈 사이클을 단축하는 방법은 무엇인가요?"),
                        new Question("사이클 병목 제거", "세일즈 사이클에서 병목 구간을 찾고 개선하는 방법은 무엇인가요?"),
                        "SalesCycle",
                        Set.of("SalesCycle", "Efficiency", "Closing")
                ),
                new FallbackQuestion(
                        new Question("리드 퀄리피케이션", "리드 퀄리피케이션 기준을 설명해 주세요."),
                        new Question("BANT 활용", "BANT 기준을 실제 영업 과정에서 어떻게 적용하시나요?"),
                        "BANT",
                        Set.of("BANT", "Qualification", "LeadScoring")
                ),
                new FallbackQuestion(
                        new Question("경쟁사 설득", "경쟁사 대비 차별점을 설득하는 방법은 무엇인가요?"),
                        new Question("차별화 전략", "가격 경쟁이 아닌 가치 경쟁으로 전환하는 방법은 무엇인가요?"),
                        "Differentiation",
                        Set.of("Differentiation", "CompetitiveSales", "Value")
                ),
                new FallbackQuestion(
                        new Question("업셀/크로스셀", "업셀/크로스셀을 성공시키는 조건은 무엇인가요?"),
                        new Question("확장 전략", "기존 고객에게 추가 판매를 성공시키는 전략은 무엇인가요?"),
                        "Upsell",
                        Set.of("Upsell", "CrossSell", "Expansion")
                ),
                new FallbackQuestion(
                        new Question("세일즈 포캐스트", "세일즈 포캐스트를 어떻게 만들고 관리하나요?"),
                        new Question("예측 정확도 개선", "포캐스트 정확도를 높이기 위한 방법은 무엇인가요?"),
                        "Forecast",
                        Set.of("Forecast", "Revenue", "DataDriven")
                ),
                new FallbackQuestion(
                        new Question("계약/법무 협업", "계약/법무 이슈 발생 시 협업 방식은 무엇인가요?"),
                        new Question("리스크 관리", "계약 리스크를 줄이기 위한 사전 대응 방법은 무엇인가요?"),
                        "Legal",
                        Set.of("Legal", "Collaboration", "RiskManagement")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        new Question("엔터프라이즈 딜 전략", "대형 딜에서 이해관계자를 공략하는 전략은 무엇인가요?"),
                        new Question("Stakeholder 전략", "복잡한 조직에서 의사결정자를 설득하는 구조는 어떻게 설계하시나요?"),
                        "EnterpriseSales",
                        Set.of("EnterpriseSales", "StakeholderMapping", "Strategy")
                ),
                new FallbackQuestion(
                        new Question("목표 vs 관계", "매출 목표와 장기 고객 관계가 충돌할 때 어떻게 의사결정하나요?"),
                        new Question("균형 전략", "단기 성과와 장기 신뢰를 동시에 확보하기 위한 전략은 무엇인가요?"),
                        "TradeOff",
                        Set.of("TradeOff", "CustomerRelationship", "Strategy")
                ),
                new FallbackQuestion(
                        new Question("가치 기반 협상", "가격 인하 요구 상황에서 협상 전략은 무엇인가요?"),
                        new Question("가치 설득", "가격 대신 가치로 설득하기 위한 핵심 포인트는 무엇인가요?"),
                        "ValueBasedSelling",
                        Set.of("ValueBasedSelling", "Negotiation", "Pricing")
                ),
                new FallbackQuestion(
                        new Question("리뉴얼 리스크 감지", "리뉴얼 리스크를 조기에 감지하는 방법은 무엇인가요?"),
                        new Question("이탈 예측", "고객 이탈 가능성을 예측하는 주요 지표는 무엇인가요?"),
                        "Renewal",
                        Set.of("Renewal", "ChurnRisk", "CustomerSuccess")
                ),
                new FallbackQuestion(
                        new Question("부서 간 충돌 조율", "세일즈와 타 부서 충돌 시 해결 방법은 무엇인가요?"),
                        new Question("조율 전략", "조직 간 목표를 정렬하기 위한 협업 구조는 어떻게 설계하시나요?"),
                        "CrossFunctional",
                        Set.of("CrossFunctional", "Alignment", "ConflictManagement")
                ),
                new FallbackQuestion(
                        new Question("클로징 부진 진단", "클로징이 안 되는 원인을 어떻게 분석하나요?"),
                        new Question("진단 프레임워크", "클로징 전환율을 개선하기 위한 분석 방법은 무엇인가요?"),
                        "PipelineAnalysis",
                        Set.of("PipelineAnalysis", "ConversionRate", "Diagnosis")
                ),
                new FallbackQuestion(
                        new Question("글로벌 영업 관리", "글로벌 영업에서 리스크를 어떻게 관리하나요?"),
                        new Question("글로벌 전략", "국가별 규제와 문화 차이를 반영한 영업 전략은 무엇인가요?"),
                        "GlobalSales",
                        Set.of("GlobalSales", "Compliance", "Process")
                ),
                new FallbackQuestion(
                        new Question("RFP 대응 전략", "대규모 RFP 대응 전략은 무엇인가요?"),
                        new Question("수주 전략", "RFP에서 경쟁 우위를 확보하기 위한 전략은 무엇인가요?"),
                        "RFP",
                        Set.of("RFP", "Enterprise", "BidStrategy")
                ),
                new FallbackQuestion(
                        new Question("세일즈 스케일업", "세일즈 조직을 스케일업하는 방법은 무엇인가요?"),
                        new Question("조직 운영 전략", "KPI와 코칭 체계를 어떻게 설계해야 하나요?"),
                        "SalesScaling",
                        Set.of("SalesScaling", "KPI", "Coaching")
                ),
                new FallbackQuestion(
                        new Question("Lost Deal 분석", "Lost deal을 분석하는 방법은 무엇인가요?"),
                        new Question("프로세스 개선", "Lost deal 분석을 통해 영업 프로세스를 개선하는 방법은 무엇인가요?"),
                        "LostDeal",
                        Set.of("LostDeal", "PostMortem", "Improvement")
                )
        ));

        return Map.copyOf(map);
    }
}