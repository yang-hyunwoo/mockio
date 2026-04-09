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
                                "그래픽 디자이너의 역할은 무엇인가요?"
                        ),
                        new Question(
                                "디자이너 역할의 확장",
                                "그래픽 디자이너가 단순히 시각물을 만드는 것을 넘어 브랜드와 사용자 경험에 기여하는 방식은 무엇인가요?"
                        ),
                        "GraphicDesign",
                        Set.of("GraphicDesign", "Role", "Visual")
                ),
                new FallbackQuestion(
                        new Question(
                                "UI vs UX",
                                "UI와 UX의 차이는 무엇인가요?"
                        ),
                        new Question(
                                "UI/UX 연결 구조",
                                "UI와 UX가 서로 분리된 개념이 아니라 함께 작동해야 하는 이유를 실제 서비스 예시와 함께 설명해 주세요."
                        ),
                        "UI",
                        Set.of("UI", "UX", "Design")
                ),
                new FallbackQuestion(
                        new Question(
                                "컬러 이론",
                                "컬러 이론이 중요한 이유는 무엇인가요?"
                        ),
                        new Question(
                                "컬러 선택의 실무 기준",
                                "브랜드 컬러와 사용자 가독성, 접근성을 함께 고려해 색상을 선택할 때 어떤 기준이 필요할까요?"
                        ),
                        "ColorTheory",
                        Set.of("ColorTheory", "Visual", "Brand")
                ),
                new FallbackQuestion(
                        new Question(
                                "타이포그래피",
                                "타이포그래피란 무엇인가요?"
                        ),
                        new Question(
                                "타이포그래피와 사용성",
                                "타이포그래피가 단순히 미적인 요소를 넘어서 정보 전달력과 사용성에 어떤 영향을 주는지 설명해 주세요."
                        ),
                        "Typography",
                        Set.of("Typography", "Visual", "Layout")
                ),
                new FallbackQuestion(
                        new Question(
                                "디자인 가이드라인",
                                "디자인 가이드라인은 왜 필요한가요?"
                        ),
                        new Question(
                                "가이드라인 운영 효과",
                                "디자인 가이드라인이 팀 협업 속도와 결과물 일관성에 어떤 영향을 주는지 설명해 주세요."
                        ),
                        "Guideline",
                        Set.of("Guideline", "Consistency", "Design")
                ),
                new FallbackQuestion(
                        new Question(
                                "웹 vs 모바일 디자인",
                                "웹과 모바일 디자인의 차이는 무엇인가요?"
                        ),
                        new Question(
                                "플랫폼별 설계 차이",
                                "웹과 모바일에서 화면 크기, 입력 방식, 사용 맥락 차이가 디자인 결정에 어떻게 영향을 주나요?"
                        ),
                        "WebDesign",
                        Set.of("WebDesign", "MobileDesign", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "아이콘 디자인",
                                "아이콘 디자인의 중요성은 무엇인가요?"
                        ),
                        new Question(
                                "아이콘의 전달력",
                                "좋은 아이콘이 텍스트를 대체하거나 보완하면서 사용자의 인지 부담을 줄이는 이유는 무엇인가요?"
                        ),
                        "Icon",
                        Set.of("Icon", "Visual", "Usability")
                ),
                new FallbackQuestion(
                        new Question(
                                "와이어프레임",
                                "와이어프레임이란 무엇인가요?"
                        ),
                        new Question(
                                "와이어프레임 활용 목적",
                                "와이어프레임 단계에서 시각 디자인보다 구조와 사용자 흐름 검증이 중요한 이유는 무엇인가요?"
                        ),
                        "Wireframe",
                        Set.of("Wireframe", "UX", "Prototype")
                ),
                new FallbackQuestion(
                        new Question(
                                "사용자 중심 디자인",
                                "사용자 관점 디자인이 중요한 이유는 무엇인가요?"
                        ),
                        new Question(
                                "사용자 중심 설계 기준",
                                "디자이너의 취향보다 사용자 행동과 니즈를 우선해야 하는 이유를 설명해 주세요."
                        ),
                        "UserCentered",
                        Set.of("UserCentered", "UX", "Research")
                ),
                new FallbackQuestion(
                        new Question(
                                "디자인 피드백",
                                "디자인 피드백을 어떻게 수용하나요?"
                        ),
                        new Question(
                                "피드백 반영 우선순위",
                                "여러 이해관계자의 피드백이 상충할 때 어떤 기준으로 반영 여부를 결정해야 하나요?"
                        ),
                        "Feedback",
                        Set.of("Feedback", "Collaboration", "Process")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        new Question(
                                "디자인 시스템 필요성",
                                "디자인 시스템이 필요한 이유는 무엇인가요?"
                        ),
                        new Question(
                                "디자인 시스템 도입 효과",
                                "디자인 시스템이 대규모 서비스에서 일관성뿐 아니라 협업 효율과 유지보수성에 주는 이점을 설명해 주세요."
                        ),
                        "DesignSystem",
                        Set.of("DesignSystem", "Consistency", "Scalability")
                ),
                new FallbackQuestion(
                        new Question(
                                "접근성 디자인",
                                "접근성(Accessibility)을 고려한 디자인이란 무엇인가요?"
                        ),
                        new Question(
                                "접근성 적용 실무",
                                "색 대비, 키보드 탐색, 스크린리더 호환성을 디자인 단계에서 어떻게 고려할 수 있나요?"
                        ),
                        "Accessibility",
                        Set.of("Accessibility", "InclusiveDesign", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "UX 리서치",
                                "UX 리서치는 어떻게 진행하나요?"
                        ),
                        new Question(
                                "리서치 결과 해석",
                                "UX 리서치에서 수집한 사용자 인터뷰와 행동 데이터를 실제 디자인 개선안으로 연결하는 방법을 설명해 주세요."
                        ),
                        "UXResearch",
                        Set.of("UXResearch", "UserInterview", "Data")
                ),
                new FallbackQuestion(
                        new Question(
                                "반응형 디자인",
                                "반응형 디자인의 핵심 포인트는 무엇인가요?"
                        ),
                        new Question(
                                "반응형 우선순위 설계",
                                "화면 크기별로 동일한 UI를 단순 축소하지 않고 우선순위를 재배치해야 하는 이유는 무엇인가요?"
                        ),
                        "Responsive",
                        Set.of("Responsive", "Layout", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "프로토타입 활용",
                                "프로토타입을 사용하는 이유는 무엇인가요?"
                        ),
                        new Question(
                                "프로토타입 검증 가치",
                                "프로토타입이 실제 개발 전에 사용자 흐름과 인터랙션 문제를 발견하는 데 어떻게 도움이 되나요?"
                        ),
                        "Prototype",
                        Set.of("Prototype", "Testing", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "브랜드 아이덴티티",
                                "브랜드 아이덴티티를 디자인에 어떻게 반영하나요?"
                        ),
                        new Question(
                                "브랜드와 사용성 균형",
                                "브랜드 아이덴티티를 강하게 반영하면서도 사용성을 해치지 않으려면 어떤 균형이 필요할까요?"
                        ),
                        "BrandIdentity",
                        Set.of("BrandIdentity", "Visual", "Consistency")
                ),
                new FallbackQuestion(
                        new Question(
                                "사용성 테스트",
                                "사용성 테스트는 언제 필요한가요?"
                        ),
                        new Question(
                                "테스트 시점 판단",
                                "기획 초기, 프로토타입 단계, 출시 후 중 어떤 시점에 어떤 목적의 사용성 테스트를 진행해야 하나요?"
                        ),
                        "UsabilityTest",
                        Set.of("UsabilityTest", "UX", "Validation")
                ),
                new FallbackQuestion(
                        new Question(
                                "디자인 일관성",
                                "디자인 일관성을 유지하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "일관성과 유연성 조정",
                                "일관성을 유지하면서도 제품별 특성이나 실험적 시도를 허용하려면 어떤 운영 방식이 필요할까요?"
                        ),
                        "Consistency",
                        Set.of("Consistency", "DesignSystem", "UI")
                ),
                new FallbackQuestion(
                        new Question(
                                "개발자 협업",
                                "협업 시 개발자와의 커뮤니케이션 방법은 무엇인가요?"
                        ),
                        new Question(
                                "핸드오프 품질 관리",
                                "디자인 의도와 구현 가능성을 맞추기 위해 개발자와 협업할 때 어떤 문서화와 커뮤니케이션이 중요할까요?"
                        ),
                        "Collaboration",
                        Set.of("Collaboration", "Developer", "Communication")
                ),
                new FallbackQuestion(
                        new Question(
                                "디자인 툴 선택",
                                "디자인 툴 선택 기준은 무엇인가요?"
                        ),
                        new Question(
                                "툴 선택의 조직 관점",
                                "개인의 숙련도보다 팀 협업 방식과 버전 관리 관점에서 디자인 툴을 선택해야 하는 이유는 무엇인가요?"
                        ),
                        "DesignTool",
                        Set.of("DesignTool", "Workflow", "Productivity")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        new Question(
                                "디자인 시스템 확장",
                                "대규모 서비스에서 디자인 시스템을 확장하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "시스템 확장 운영 전략",
                                "여러 제품과 조직이 함께 사용하는 디자인 시스템을 확장할 때 거버넌스와 컴포넌트 관리 전략은 어떻게 가져가야 하나요?"
                        ),
                        "DesignSystem",
                        Set.of("DesignSystem", "Scalability", "Architecture")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 기반 UX",
                                "데이터 기반 UX 개선 사례를 설명해 주세요."
                        ),
                        new Question(
                                "정량·정성 데이터 결합",
                                "UX 개선에서 클릭률이나 전환율 같은 정량 데이터와 인터뷰 같은 정성 데이터를 어떻게 함께 해석해야 하나요?"
                        ),
                        "DataDriven",
                        Set.of("DataDriven", "UX", "Analytics")
                ),
                new FallbackQuestion(
                        new Question(
                                "글로벌 디자인",
                                "글로벌 서비스를 위한 디자인 고려사항은 무엇인가요?"
                        ),
                        new Question(
                                "현지화 디자인 전략",
                                "다국어, 문화 차이, 법적 요구사항을 반영한 글로벌 디자인에서 가장 먼저 고려해야 할 우선순위는 무엇인가요?"
                        ),
                        "Localization",
                        Set.of("Localization", "Global", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "접근성 vs 심미성",
                                "접근성과 심미성을 동시에 만족시키는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "디자인 균형 의사결정",
                                "브랜드의 시각적 완성도를 유지하면서도 접근성을 충족하기 위해 어떤 설계 원칙을 적용할 수 있나요?"
                        ),
                        "Accessibility",
                        Set.of("Accessibility", "Aesthetics", "Balance")
                ),
                new FallbackQuestion(
                        new Question(
                                "디자인 KPI 측정",
                                "디자인 변경이 KPI에 미치는 영향을 어떻게 측정하나요?"
                        ),
                        new Question(
                                "디자인 효과 검증 방법",
                                "디자인 변경의 효과를 단순 클릭률이 아니라 전환율, 이탈률, 만족도까지 포함해 어떻게 검증할 수 있나요?"
                        ),
                        "KPI",
                        Set.of("KPI", "Experiment", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "플로우 단순화",
                                "복잡한 사용자 플로우를 단순화하는 전략은 무엇인가요?"
                        ),
                        new Question(
                                "복잡도 축소 기준",
                                "사용자 플로우를 단순화할 때 단계를 무조건 줄이는 것이 아니라 핵심 의사결정 지점을 어떻게 재설계해야 하나요?"
                        ),
                        "UserFlow",
                        Set.of("UserFlow", "Simplification", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "디자인 기술 부채",
                                "디자인 기술 부채를 관리하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "디자인 부채 상환 전략",
                                "운영 중인 서비스에서 누적된 디자인 부채를 기능 개발과 병행해 정리하려면 어떤 우선순위 기준이 필요할까요?"
                        ),
                        "DesignDebt",
                        Set.of("DesignDebt", "Refactoring", "System")
                ),
                new FallbackQuestion(
                        new Question(
                                "A/B 테스트 디자인",
                                "A/B 테스트를 디자인에 어떻게 활용하나요?"
                        ),
                        new Question(
                                "실험 설계 주의점",
                                "디자인 A/B 테스트에서 가설 설정, 실험군 분리, 결과 해석 시 주의해야 할 점은 무엇인가요?"
                        ),
                        "ABTest",
                        Set.of("ABTest", "Experiment", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "브랜드 리뉴얼 리스크",
                                "브랜드 리뉴얼 시 주요 리스크는 무엇인가요?"
                        ),
                        new Question(
                                "리뉴얼 변화 관리",
                                "브랜드 리뉴얼이 기존 사용자에게 혼란을 주지 않도록 점진적으로 전환하는 방법은 무엇인가요?"
                        ),
                        "Brand",
                        Set.of("Brand", "Risk", "Strategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "디자인 설득",
                                "디자인 의사결정을 설득하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "이해관계자 설득 구조",
                                "디자인 제안을 경영진, PM, 개발자에게 각각 다르게 설득해야 하는 이유와 방법을 설명해 주세요."
                        ),
                        "Communication",
                        Set.of("Communication", "Stakeholder", "Strategy")
                )
        ));

        return Map.copyOf(map);
    }
}