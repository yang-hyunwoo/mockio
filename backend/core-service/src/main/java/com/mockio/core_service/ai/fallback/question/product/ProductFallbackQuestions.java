package com.mockio.core_service.ai.fallback.question.product;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.core_service.ai.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.generator.question.Question;

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
                        new Question("PM 역할", "PM의 역할은 무엇인가요?"),
                        new Question("PM 역할 확장", "PM이 단순 요구사항 관리자를 넘어 제품 성과 책임자로서 가져야 할 역할은 무엇인가요?"),
                        "PM",
                        Set.of("PM", "ProductManagement", "Role")
                ),
                new FallbackQuestion(
                        new Question("PRD 개념", "기획서(PRD)란 무엇인가요?"),
                        new Question("PRD 품질 기준", "좋은 PRD가 개발 효율과 제품 품질에 어떤 영향을 주는지 설명해 주세요."),
                        "PRD",
                        Set.of("PRD", "Requirement", "Documentation")
                ),
                new FallbackQuestion(
                        new Question("요구사항 vs 기능 정의", "요구사항과 기능 정의의 차이는 무엇인가요?"),
                        new Question("요구사항 구체화", "모호한 요구사항을 실제 구현 가능한 기능으로 구체화하는 과정은 어떻게 진행하시나요?"),
                        "Requirement",
                        Set.of("Requirement", "Feature", "Specification")
                ),
                new FallbackQuestion(
                        new Question("우선순위 기준", "우선순위를 정할 때 어떤 기준을 쓰나요?"),
                        new Question("우선순위 프레임워크", "RICE, ICE 같은 프레임워크를 실제로 어떻게 적용하시나요?"),
                        "Prioritization",
                        Set.of("Prioritization", "DecisionMaking", "Strategy")
                ),
                new FallbackQuestion(
                        new Question("MVP 개념", "MVP란 무엇인가요?"),
                        new Question("MVP 범위 설정", "MVP를 너무 크게 또는 작게 잡지 않기 위한 기준은 무엇인가요?"),
                        "MVP",
                        Set.of("MVP", "Lean", "Product")
                ),
                new FallbackQuestion(
                        new Question("페르소나 정의", "사용자 페르소나란 무엇인가요?"),
                        new Question("페르소나 활용", "페르소나를 실제 제품 의사결정에 어떻게 활용하시나요?"),
                        "Persona",
                        Set.of("Persona", "UserResearch", "Target")
                ),
                new FallbackQuestion(
                        new Question("유저 스토리 목적", "유저 스토리는 왜 작성하나요?"),
                        new Question("스토리 품질 기준", "좋은 유저 스토리가 개발자와 협업 효율을 높이는 이유는 무엇인가요?"),
                        "UserStory",
                        Set.of("UserStory", "Agile", "Requirement")
                ),
                new FallbackQuestion(
                        new Question("릴리즈 노트 목적", "릴리즈 노트의 목적은 무엇인가요?"),
                        new Question("릴리즈 커뮤니케이션", "릴리즈 노트를 사용자와 내부 이해관계자에게 어떻게 다르게 전달해야 하나요?"),
                        "ReleaseNote",
                        Set.of("ReleaseNote", "Communication", "Product")
                ),
                new FallbackQuestion(
                        new Question("제품 커뮤니케이션", "커뮤니케이션이 중요한 이유는 무엇인가요?"),
                        new Question("커뮤니케이션 구조", "PM으로서 다양한 이해관계자 간 커뮤니케이션을 어떻게 구조화하시나요?"),
                        "Communication",
                        Set.of("Communication", "Stakeholder", "Collaboration")
                ),
                new FallbackQuestion(
                        new Question("성공 기준", "성공적인 제품의 기준은 무엇이라고 생각하나요?"),
                        new Question("성공 지표 정의", "제품 성공을 측정하기 위한 핵심 KPI를 어떻게 정의하시나요?"),
                        "ProductSuccess",
                        Set.of("ProductSuccess", "KPI", "UserValue")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        new Question("제품 로드맵 수립", "제품 로드맵을 수립하는 방법을 설명해 주세요."),
                        new Question("로드맵 우선순위", "로드맵을 구성할 때 단기 과제와 장기 전략을 어떻게 균형 잡나요?"),
                        "Roadmap",
                        Set.of("Roadmap", "Strategy", "Planning")
                ),
                new FallbackQuestion(
                        new Question("KPI/OKR 관리", "KPI/OKR을 설정하고 관리하는 방법은 무엇인가요?"),
                        new Question("지표 정렬", "조직 목표와 제품 KPI를 어떻게 정렬시키나요?"),
                        "KPI",
                        Set.of("KPI", "OKR", "Performance")
                ),
                new FallbackQuestion(
                        new Question("A/B 테스트 설계", "A/B 테스트를 설계할 때 고려해야 할 사항은 무엇인가요?"),
                        new Question("실험 해석", "A/B 테스트 결과를 의사결정으로 연결할 때 주의할 점은 무엇인가요?"),
                        "ABTest",
                        Set.of("ABTest", "Experiment", "Validation")
                ),
                new FallbackQuestion(
                        new Question("스코프 크립 관리", "요구사항 변경(스코프 크립)을 어떻게 관리하나요?"),
                        new Question("변경 통제 전략", "스코프 변경을 수용할지 거절할지 판단 기준은 무엇인가요?"),
                        "ScopeCreep",
                        Set.of("ScopeCreep", "ChangeManagement", "Execution")
                ),
                new FallbackQuestion(
                        new Question("이해관계자 조율", "이해관계자(개발/디자인/비즈니스) 조율은 어떻게 하나요?"),
                        new Question("의사결정 정렬", "이해관계자 간 목표가 다를 때 의사결정을 어떻게 정렬시키나요?"),
                        "Alignment",
                        Set.of("Stakeholder", "Alignment", "Communication")
                ),
                new FallbackQuestion(
                        new Question("사용자 리서치", "사용자 리서치를 통해 인사이트를 도출하는 방법은 무엇인가요?"),
                        new Question("인사이트 활용", "리서치 결과를 실제 기능 개선으로 연결하는 방법은 무엇인가요?"),
                        "UserResearch",
                        Set.of("UserResearch", "Insight", "Discovery")
                ),
                new FallbackQuestion(
                        new Question("데이터 vs 직관", "데이터 기반 의사결정과 직관 기반 의사결정의 균형은 어떻게 맞추나요?"),
                        new Question("판단 기준", "데이터가 부족한 상황에서 의사결정을 내리는 기준은 무엇인가요?"),
                        "DataDriven",
                        Set.of("DecisionMaking", "DataDriven", "Judgment")
                ),
                new FallbackQuestion(
                        new Question("품질 vs 일정", "제품 품질과 일정 사이의 트레이드오프를 어떻게 다루나요?"),
                        new Question("트레이드오프 판단", "품질을 희생할지 일정을 미룰지 결정할 때 어떤 기준을 사용하시나요?"),
                        "TradeOff",
                        Set.of("TradeOff", "Quality", "Delivery")
                ),
                new FallbackQuestion(
                        new Question("지표 개선 접근", "제품 지표를 모니터링하고 개선하는 접근은 무엇인가요?"),
                        new Question("지표 기반 개선", "전환율이나 리텐션을 개선하기 위한 실험 설계 방법은 무엇인가요?"),
                        "Retention",
                        Set.of("Metrics", "Retention", "Conversion")
                ),
                new FallbackQuestion(
                        new Question("경쟁사 분석 반영", "경쟁사 분석을 제품 전략에 어떻게 반영하나요?"),
                        new Question("차별화 전략", "경쟁사 대비 차별화된 기능을 어떻게 도출하시나요?"),
                        "Differentiation",
                        Set.of("Competitor", "Strategy", "Differentiation")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        new Question("전사 정렬", "전사 목표와 제품 로드맵을 정렬시키는 방법은 무엇인가요?"),
                        new Question("조직 정렬 전략", "여러 조직 간 목표를 일관되게 맞추기 위한 구조는 어떻게 설계하시나요?"),
                        "Alignment",
                        Set.of("Alignment", "Strategy", "Roadmap")
                ),
                new FallbackQuestion(
                        new Question("지표 착시 방지", "지표가 좋아 보이지만 실제 가치가 떨어지는 상황을 어떻게 방지하나요?"),
                        new Question("지표 해석 오류", "잘못된 KPI가 제품 방향을 왜곡하는 사례와 해결 방법은 무엇인가요?"),
                        "MetricTrap",
                        Set.of("MetricTrap", "Value", "Analytics")
                ),
                new FallbackQuestion(
                        new Question("실험 문화 정착", "실험 문화가 없는 조직에서 A/B 테스트를 정착시키는 방법은 무엇인가요?"),
                        new Question("문화 변화 전략", "실험 실패를 허용하는 조직 문화를 어떻게 만들 수 있나요?"),
                        "Experimentation",
                        Set.of("Experimentation", "Culture", "ABTest")
                ),
                new FallbackQuestion(
                        new Question("매출 vs UX", "매출과 사용자 경험이 충돌할 때 의사결정을 어떻게 하나요?"),
                        new Question("우선순위 기준", "단기 매출과 장기 사용자 경험 중 어떤 기준으로 선택하시나요?"),
                        "TradeOff",
                        Set.of("TradeOff", "UX", "Revenue")
                ),
                new FallbackQuestion(
                        new Question("R&R 경계 해결", "권한/책임 경계가 불명확할 때 해결 방법은 무엇인가요?"),
                        new Question("거버넌스 설계", "R&R을 명확히 하기 위한 조직 구조와 프로세스는 어떻게 설계하시나요?"),
                        "RoleClarity",
                        Set.of("RoleClarity", "Governance", "Organization")
                ),
                new FallbackQuestion(
                        new Question("North Star Metric", "북극성 지표를 정의하는 방법은 무엇인가요?"),
                        new Question("지표 선정 기준", "North Star Metric을 잘못 정의했을 때 어떤 문제가 발생하나요?"),
                        "NorthStarMetric",
                        Set.of("NorthStarMetric", "Growth", "KPI")
                ),
                new FallbackQuestion(
                        new Question("리텐션 급감 대응", "리텐션이 급감했을 때 어떻게 대응하나요?"),
                        new Question("원인 분석 구조", "리텐션 감소 원인을 데이터 기반으로 분석하는 절차는 무엇인가요?"),
                        "Retention",
                        Set.of("Retention", "Analysis", "Recovery")
                ),
                new FallbackQuestion(
                        new Question("글로벌 확장 리스크", "글로벌 확장 시 주요 리스크는 무엇인가요?"),
                        new Question("현지화 전략", "다국가 확장에서 제품과 UX를 어떻게 현지화해야 하나요?"),
                        "Localization",
                        Set.of("Global", "Localization", "Risk")
                ),
                new FallbackQuestion(
                        new Question("기술 부채 조정", "기술 부채가 제품 속도를 저해할 때 어떻게 대응하나요?"),
                        new Question("부채 우선순위", "기술 부채와 신규 기능 개발 우선순위를 어떻게 조정하시나요?"),
                        "TechDebt",
                        Set.of("TechDebt", "Prioritization", "ProductStrategy")
                ),
                new FallbackQuestion(
                        new Question("피벗 신호", "제품 피벗이 필요한 신호는 무엇인가요?"),
                        new Question("피벗 실행", "피벗을 결정하고 실행하는 과정에서 어떤 데이터와 기준을 사용하시나요?"),
                        "Pivot",
                        Set.of("Pivot", "Signal", "ProductStrategy")
                )
        ));

        return Map.copyOf(map);
    }
}