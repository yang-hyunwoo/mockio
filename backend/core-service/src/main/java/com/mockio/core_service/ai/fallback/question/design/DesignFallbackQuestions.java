package com.mockio.core_service.ai.fallback.question.design;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.core_service.ai.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.generator.question.Question;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DesignFallbackQuestions {

    private DesignFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<FallbackQuestion>> byDifficulty() {
        Map<InterviewDifficulty, List<FallbackQuestion>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                new FallbackQuestion(
                        new Question(
                                "그래픽 디자이너 역할",
                                "그래픽 디자이너의 역할은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "GraphicDesign",
                        Set.of("GraphicDesign", "Role", "Visual")
                ),
                new FallbackQuestion(
                        new Question(
                                "UI vs UX",
                                "UI와 UX의 차이는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "UI",
                        Set.of("UI", "UX", "Design")
                ),
                new FallbackQuestion(
                        new Question(
                                "컬러 이론",
                                "컬러 이론이 중요한 이유는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "ColorTheory",
                        Set.of("ColorTheory", "Visual", "Brand")
                ),
                new FallbackQuestion(
                        new Question(
                                "타이포그래피",
                                "타이포그래피란 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Typography",
                        Set.of("Typography", "Visual", "Layout")
                ),
                new FallbackQuestion(
                        new Question(
                                "디자인 가이드라인",
                                "디자인 가이드라인은 왜 필요한가요?",
                                "BASIC",
                                null
                        ),
                        "Guideline",
                        Set.of("Guideline", "Consistency", "Design")
                ),
                new FallbackQuestion(
                        new Question(
                                "웹 vs 모바일 디자인",
                                "웹과 모바일 디자인의 차이는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "WebDesign",
                        Set.of("WebDesign", "MobileDesign", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "아이콘 디자인",
                                "아이콘 디자인의 중요성은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Icon",
                        Set.of("Icon", "Visual", "Usability")
                ),
                new FallbackQuestion(
                        new Question(
                                "와이어프레임",
                                "와이어프레임이란 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Wireframe",
                        Set.of("Wireframe", "UX", "Prototype")
                ),
                new FallbackQuestion(
                        new Question(
                                "사용자 중심 디자인",
                                "사용자 관점 디자인이 중요한 이유는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "UserCentered",
                        Set.of("UserCentered", "UX", "Research")
                ),
                new FallbackQuestion(
                        new Question(
                                "디자인 피드백",
                                "디자인 피드백을 어떻게 수용하나요?",
                                "BASIC",
                                null
                        ),
                        "Feedback",
                        Set.of("Feedback", "Collaboration", "Process")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        new Question(
                                "디자인 시스템 필요성",
                                "디자인 시스템이 필요한 이유는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "DesignSystem",
                        Set.of("DesignSystem", "Consistency", "Scalability")
                ),
                new FallbackQuestion(
                        new Question(
                                "접근성 디자인",
                                "접근성(Accessibility)을 고려한 디자인이란 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Accessibility",
                        Set.of("Accessibility", "InclusiveDesign", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "UX 리서치",
                                "UX 리서치는 어떻게 진행하나요?",
                                "BASIC",
                                null
                        ),
                        "UXResearch",
                        Set.of("UXResearch", "UserInterview", "Data")
                ),
                new FallbackQuestion(
                        new Question(
                                "반응형 디자인",
                                "반응형 디자인의 핵심 포인트는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Responsive",
                        Set.of("Responsive", "Layout", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "프로토타입 활용",
                                "프로토타입을 사용하는 이유는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Prototype",
                        Set.of("Prototype", "Testing", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "브랜드 아이덴티티",
                                "브랜드 아이덴티티를 디자인에 어떻게 반영하나요?",
                                "BASIC",
                                null
                        ),
                        "BrandIdentity",
                        Set.of("BrandIdentity", "Visual", "Consistency")
                ),
                new FallbackQuestion(
                        new Question(
                                "사용성 테스트",
                                "사용성 테스트는 언제 필요한가요?",
                                "BASIC",
                                null
                        ),
                        "UsabilityTest",
                        Set.of("UsabilityTest", "UX", "Validation")
                ),
                new FallbackQuestion(
                        new Question(
                                "디자인 일관성",
                                "디자인 일관성을 유지하는 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Consistency",
                        Set.of("Consistency", "DesignSystem", "UI")
                ),
                new FallbackQuestion(
                        new Question(
                                "개발자 협업",
                                "협업 시 개발자와의 커뮤니케이션 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Collaboration",
                        Set.of("Collaboration", "Developer", "Communication")
                ),
                new FallbackQuestion(
                        new Question(
                                "디자인 툴 선택",
                                "디자인 툴 선택 기준은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "DesignTool",
                        Set.of("DesignTool", "Workflow", "Productivity")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        new Question(
                                "디자인 시스템 확장",
                                "대규모 서비스에서 디자인 시스템을 확장하는 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "DesignSystem",
                        Set.of("DesignSystem", "Scalability", "Architecture")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 기반 UX",
                                "데이터 기반 UX 개선 사례를 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "DataDriven",
                        Set.of("DataDriven", "UX", "Analytics")
                ),
                new FallbackQuestion(
                        new Question(
                                "글로벌 디자인",
                                "글로벌 서비스를 위한 디자인 고려사항은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Localization",
                        Set.of("Localization", "Global", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "접근성 vs 심미성",
                                "접근성과 심미성을 동시에 만족시키는 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Accessibility",
                        Set.of("Accessibility", "Aesthetics", "Balance")
                ),
                new FallbackQuestion(
                        new Question(
                                "디자인 KPI 측정",
                                "디자인 변경이 KPI에 미치는 영향을 어떻게 측정하나요?",
                                "BASIC",
                                null
                        ),
                        "KPI",
                        Set.of("KPI", "Experiment", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "플로우 단순화",
                                "복잡한 사용자 플로우를 단순화하는 전략은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "UserFlow",
                        Set.of("UserFlow", "Simplification", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "디자인 기술 부채",
                                "디자인 기술 부채를 관리하는 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "DesignDebt",
                        Set.of("DesignDebt", "Refactoring", "System")
                ),
                new FallbackQuestion(
                        new Question(
                                "A/B 테스트 디자인",
                                "A/B 테스트를 디자인에 어떻게 활용하나요?",
                                "BASIC",
                                null
                        ),
                        "ABTest",
                        Set.of("ABTest", "Experiment", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "브랜드 리뉴얼 리스크",
                                "브랜드 리뉴얼 시 주요 리스크는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Brand",
                        Set.of("Brand", "Risk", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "디자인 설득",
                                "디자인 의사결정을 설득하는 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Communication",
                        Set.of("Communication", "Stakeholder", "Strategy")
                )
        ));

        return Map.copyOf(map);
    }
}