package com.mockio.ai_service.fallback.question.design;

import com.mockio.ai_service.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class DesignFallbackQuestions {

    private DesignFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<FallbackQuestion>> byDifficulty() {
        Map<InterviewDifficulty, List<FallbackQuestion>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                new FallbackQuestion(
                        "그래픽 디자이너 역할",
                        "그래픽 디자이너의 역할은 무엇인가요?",
                        List.of("GraphicDesign", "Role", "Visual")
                ),
                new FallbackQuestion(
                        "UI vs UX",
                        "UI와 UX의 차이는 무엇인가요?",
                        List.of("UI", "UX", "Design")
                ),
                new FallbackQuestion(
                        "컬러 이론",
                        "컬러 이론이 중요한 이유는 무엇인가요?",
                        List.of("ColorTheory", "Visual", "Brand")
                ),
                new FallbackQuestion(
                        "타이포그래피",
                        "타이포그래피란 무엇인가요?",
                        List.of("Typography", "Visual", "Layout")
                ),
                new FallbackQuestion(
                        "디자인 가이드라인",
                        "디자인 가이드라인은 왜 필요한가요?",
                        List.of("Guideline", "Consistency", "Design")
                ),
                new FallbackQuestion(
                        "웹 vs 모바일 디자인",
                        "웹과 모바일 디자인의 차이는 무엇인가요?",
                        List.of("WebDesign", "MobileDesign", "UX")
                ),
                new FallbackQuestion(
                        "아이콘 디자인",
                        "아이콘 디자인의 중요성은 무엇인가요?",
                        List.of("Icon", "Visual", "Usability")
                ),
                new FallbackQuestion(
                        "와이어프레임",
                        "와이어프레임이란 무엇인가요?",
                        List.of("Wireframe", "UX", "Prototype")
                ),
                new FallbackQuestion(
                        "사용자 중심 디자인",
                        "사용자 관점 디자인이 중요한 이유는 무엇인가요?",
                        List.of("UserCentered", "UX", "Research")
                ),
                new FallbackQuestion(
                        "디자인 피드백",
                        "디자인 피드백을 어떻게 수용하나요?",
                        List.of("Feedback", "Collaboration", "Process")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        "디자인 시스템 필요성",
                        "디자인 시스템이 필요한 이유는 무엇인가요?",
                        List.of("DesignSystem", "Consistency", "Scalability")
                ),
                new FallbackQuestion(
                        "접근성 디자인",
                        "접근성(Accessibility)을 고려한 디자인이란 무엇인가요?",
                        List.of("Accessibility", "InclusiveDesign", "UX")
                ),
                new FallbackQuestion(
                        "UX 리서치",
                        "UX 리서치는 어떻게 진행하나요?",
                        List.of("UXResearch", "UserInterview", "Data")
                ),
                new FallbackQuestion(
                        "반응형 디자인",
                        "반응형 디자인의 핵심 포인트는 무엇인가요?",
                        List.of("Responsive", "Layout", "UX")
                ),
                new FallbackQuestion(
                        "프로토타입 활용",
                        "프로토타입을 사용하는 이유는 무엇인가요?",
                        List.of("Prototype", "Testing", "UX")
                ),
                new FallbackQuestion(
                        "브랜드 아이덴티티",
                        "브랜드 아이덴티티를 디자인에 어떻게 반영하나요?",
                        List.of("BrandIdentity", "Visual", "Consistency")
                ),
                new FallbackQuestion(
                        "사용성 테스트",
                        "사용성 테스트는 언제 필요한가요?",
                        List.of("UsabilityTest", "UX", "Validation")
                ),
                new FallbackQuestion(
                        "디자인 일관성",
                        "디자인 일관성을 유지하는 방법은 무엇인가요?",
                        List.of("Consistency", "DesignSystem", "UI")
                ),
                new FallbackQuestion(
                        "개발자 협업",
                        "협업 시 개발자와의 커뮤니케이션 방법은 무엇인가요?",
                        List.of("Collaboration", "Developer", "Communication")
                ),
                new FallbackQuestion(
                        "디자인 툴 선택",
                        "디자인 툴 선택 기준은 무엇인가요?",
                        List.of("DesignTool", "Workflow", "Productivity")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        "디자인 시스템 확장",
                        "대규모 서비스에서 디자인 시스템을 확장하는 방법은 무엇인가요?",
                        List.of("DesignSystem", "Scalability", "Architecture")
                ),
                new FallbackQuestion(
                        "데이터 기반 UX",
                        "데이터 기반 UX 개선 사례를 설명해 주세요.",
                        List.of("DataDriven", "UX", "Analytics")
                ),
                new FallbackQuestion(
                        "글로벌 디자인",
                        "글로벌 서비스를 위한 디자인 고려사항은 무엇인가요?",
                        List.of("Localization", "Global", "UX")
                ),
                new FallbackQuestion(
                        "접근성 vs 심미성",
                        "접근성과 심미성을 동시에 만족시키는 방법은 무엇인가요?",
                        List.of("Accessibility", "Aesthetics", "Balance")
                ),
                new FallbackQuestion(
                        "디자인 KPI 측정",
                        "디자인 변경이 KPI에 미치는 영향을 어떻게 측정하나요?",
                        List.of("KPI", "Experiment", "UX")
                ),
                new FallbackQuestion(
                        "플로우 단순화",
                        "복잡한 사용자 플로우를 단순화하는 전략은 무엇인가요?",
                        List.of("UserFlow", "Simplification", "UX")
                ),
                new FallbackQuestion(
                        "디자인 기술 부채",
                        "디자인 기술 부채를 관리하는 방법은 무엇인가요?",
                        List.of("DesignDebt", "Refactoring", "System")
                ),
                new FallbackQuestion(
                        "A/B 테스트 디자인",
                        "A/B 테스트를 디자인에 어떻게 활용하나요?",
                        List.of("ABTest", "Experiment", "UX")
                ),
                new FallbackQuestion(
                        "브랜드 리뉴얼 리스크",
                        "브랜드 리뉴얼 시 주요 리스크는 무엇인가요?",
                        List.of("Brand", "Risk", "Strategy")
                ),
                new FallbackQuestion(
                        "디자인 설득",
                        "디자인 의사결정을 설득하는 방법은 무엇인가요?",
                        List.of("Communication", "Stakeholder", "Strategy")
                )
        ));

        return Map.copyOf(map);
    }

}
