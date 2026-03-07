package com.mockio.ai_service.fallback.question.product;

import com.mockio.ai_service.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ProductFallbackQuestions {

    private ProductFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<FallbackQuestion>> byDifficulty() {
        Map<InterviewDifficulty, List<FallbackQuestion>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                new FallbackQuestion(
                        "PM 역할",
                        "PM의 역할은 무엇인가요?",
                        Set.of("PM", "ProductManagement", "Role")
                ),
                new FallbackQuestion(
                        "PRD 개념",
                        "기획서(PRD)란 무엇인가요?",
                        Set.of("PRD", "Requirement", "Documentation")
                ),
                new FallbackQuestion(
                        "요구사항 vs 기능 정의",
                        "요구사항과 기능 정의의 차이는 무엇인가요?",
                        Set.of("Requirement", "Feature", "Specification")
                ),
                new FallbackQuestion(
                        "우선순위 기준",
                        "우선순위를 정할 때 어떤 기준을 쓰나요?",
                        Set.of("Prioritization", "DecisionMaking", "Strategy")
                ),
                new FallbackQuestion(
                        "MVP 개념",
                        "MVP란 무엇인가요?",
                        Set.of("MVP", "Lean", "Product")
                ),
                new FallbackQuestion(
                        "페르소나 정의",
                        "사용자 페르소나란 무엇인가요?",
                        Set.of("Persona", "UserResearch", "Target")
                ),
                new FallbackQuestion(
                        "유저 스토리 목적",
                        "유저 스토리는 왜 작성하나요?",
                        Set.of("UserStory", "Agile", "Requirement")
                ),
                new FallbackQuestion(
                        "릴리즈 노트 목적",
                        "릴리즈 노트의 목적은 무엇인가요?",
                        Set.of("ReleaseNote", "Communication", "Product")
                ),
                new FallbackQuestion(
                        "제품 커뮤니케이션",
                        "커뮤니케이션이 중요한 이유는 무엇인가요?",
                        Set.of("Communication", "Stakeholder", "Collaboration")
                ),
                new FallbackQuestion(
                        "성공 기준",
                        "성공적인 제품의 기준은 무엇이라고 생각하나요?",
                        Set.of("ProductSuccess", "KPI", "UserValue")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        "제품 로드맵 수립",
                        "제품 로드맵을 수립하는 방법을 설명해 주세요.",
                        Set.of("Roadmap", "Strategy", "Planning")
                ),
                new FallbackQuestion(
                        "KPI/OKR 관리",
                        "KPI/OKR을 설정하고 관리하는 방법은 무엇인가요?",
                        Set.of("KPI", "OKR", "Performance")
                ),
                new FallbackQuestion(
                        "A/B 테스트 설계",
                        "A/B 테스트를 설계할 때 고려해야 할 사항은 무엇인가요?",
                        Set.of("ABTest", "Experiment", "Validation")
                ),
                new FallbackQuestion(
                        "스코프 크립 관리",
                        "요구사항 변경(스코프 크립)을 어떻게 관리하나요?",
                        Set.of("ScopeCreep", "ChangeManagement", "Execution")
                ),
                new FallbackQuestion(
                        "이해관계자 조율",
                        "이해관계자(개발/디자인/비즈니스) 조율은 어떻게 하나요?",
                        Set.of("Stakeholder", "Alignment", "Communication")
                ),
                new FallbackQuestion(
                        "사용자 리서치",
                        "사용자 리서치를 통해 인사이트를 도출하는 방법은 무엇인가요?",
                        Set.of("UserResearch", "Insight", "Discovery")
                ),
                new FallbackQuestion(
                        "데이터 vs 직관",
                        "데이터 기반 의사결정과 직관 기반 의사결정의 균형은 어떻게 맞추나요?",
                        Set.of("DecisionMaking", "DataDriven", "Judgment")
                ),
                new FallbackQuestion(
                        "품질 vs 일정",
                        "제품 품질(버그/안정성)과 일정 사이의 트레이드오프를 어떻게 다루나요?",
                        Set.of("TradeOff", "Quality", "Delivery")
                ),
                new FallbackQuestion(
                        "지표 개선 접근",
                        "제품 지표(전환, 리텐션 등)를 모니터링하고 개선하는 접근은 무엇인가요?",
                        Set.of("Metrics", "Retention", "Conversion")
                ),
                new FallbackQuestion(
                        "경쟁사 분석 반영",
                        "경쟁사 분석을 제품 전략에 어떻게 반영하나요?",
                        Set.of("Competitor", "Strategy", "Differentiation")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        "전사 정렬",
                        "전사 목표와 제품 로드맵을 정렬(Alignment)시키는 방법은 무엇인가요?",
                        Set.of("Alignment", "Strategy", "Roadmap")
                ),
                new FallbackQuestion(
                        "지표 착시 방지",
                        "지표가 좋아 보이는데 실제 제품 가치는 악화되는 사례를 어떻게 방지하나요?",
                        Set.of("MetricTrap", "Value", "Analytics")
                ),
                new FallbackQuestion(
                        "실험 문화 정착",
                        "실험(Experimentation) 문화가 없는 조직에서 A/B 테스트를 정착시키는 방법은 무엇인가요?",
                        Set.of("Experimentation", "Culture", "ABTest")
                ),
                new FallbackQuestion(
                        "매출 vs UX",
                        "매출과 사용자 경험이 충돌할 때 의사결정을 어떻게 하나요?",
                        Set.of("TradeOff", "UX", "Revenue")
                ),
                new FallbackQuestion(
                        "R&R 경계 해결",
                        "대규모 제품에서 권한/책임(R&R) 경계가 불명확할 때 해결 방법은 무엇인가요?",
                        Set.of("RoleClarity", "Governance", "Organization")
                ),
                new FallbackQuestion(
                        "North Star Metric",
                        "제품의 북극성 지표(North Star Metric)를 정의하는 방법을 설명해 주세요.",
                        Set.of("NorthStarMetric", "Growth", "KPI")
                ),
                new FallbackQuestion(
                        "리텐션 급감 대응",
                        "리텐션이 급감했을 때 원인 분석과 대응 절차를 설명해 주세요.",
                        Set.of("Retention", "Analysis", "Recovery")
                ),
                new FallbackQuestion(
                        "글로벌 확장 리스크",
                        "다국가/다언어 제품 확장 시 주요 리스크와 대응은 무엇인가요?",
                        Set.of("Global", "Localization", "Risk")
                ),
                new FallbackQuestion(
                        "기술 부채 조정",
                        "기술 부채가 제품 속도를 저해할 때 PM으로서 어떻게 우선순위를 조정하나요?",
                        Set.of("TechDebt", "Prioritization", "ProductStrategy")
                ),
                new FallbackQuestion(
                        "피벗 신호",
                        "제품 실패를 빠르게 감지하고 피벗하는 신호와 방법은 무엇인가요?",
                        Set.of("Pivot", "Signal", "ProductStrategy")
                )
        ));

        return Map.copyOf(map);
    }

}
