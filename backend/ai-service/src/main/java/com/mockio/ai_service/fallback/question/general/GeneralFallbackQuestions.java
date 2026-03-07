package com.mockio.ai_service.fallback.question.general;

import com.mockio.ai_service.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class GeneralFallbackQuestions {

    private GeneralFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<FallbackQuestion>> byDifficulty() {
        Map<InterviewDifficulty, List<FallbackQuestion>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                new FallbackQuestion(
                        "자기소개",
                        "본인 소개를 해 주세요.",
                        Set.of("SelfIntroduction", "Communication", "Career")
                ),
                new FallbackQuestion(
                        "문제 해결 경험",
                        "최근에 해결했던 문제 경험을 말해 주세요.",
                        Set.of("ProblemSolving", "Experience", "Storytelling")
                ),
                new FallbackQuestion(
                        "강점",
                        "본인의 강점은 무엇인가요?",
                        Set.of("Strength", "SelfAwareness", "Career")
                ),
                new FallbackQuestion(
                        "약점과 보완",
                        "본인의 약점은 무엇이며 어떻게 보완하나요?",
                        Set.of("Weakness", "Improvement", "Growth")
                ),
                new FallbackQuestion(
                        "협업 가치관",
                        "협업할 때 중요하게 생각하는 것은 무엇인가요?",
                        Set.of("Collaboration", "Teamwork", "Communication")
                ),
                new FallbackQuestion(
                        "스트레스 관리",
                        "스트레스를 관리하는 방법은 무엇인가요?",
                        Set.of("StressManagement", "Resilience", "WorkLife")
                ),
                new FallbackQuestion(
                        "실수와 개선",
                        "실수를 했던 경험과 개선한 방법을 말해 주세요.",
                        Set.of("Failure", "Learning", "Improvement")
                ),
                new FallbackQuestion(
                        "학습 방법",
                        "새로운 것을 학습하는 본인만의 방법은 무엇인가요?",
                        Set.of("Learning", "Growth", "Skill")
                ),
                new FallbackQuestion(
                        "선호 업무 환경",
                        "원하는 업무 환경/문화는 무엇인가요?",
                        Set.of("Culture", "Environment", "Fit")
                ),
                new FallbackQuestion(
                        "지원 동기",
                        "지원한 이유는 무엇인가요?",
                        Set.of("Motivation", "Career", "CompanyFit")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        "갈등 해결 STAR",
                        "갈등 상황을 해결한 경험을 STAR 방식으로 설명해 주세요.",
                        Set.of("Conflict", "STAR", "ProblemSolving")
                ),
                new FallbackQuestion(
                        "우선순위 충돌",
                        "우선순위가 충돌할 때 의사결정을 어떻게 하나요?",
                        Set.of("Prioritization", "DecisionMaking", "Execution")
                ),
                new FallbackQuestion(
                        "촉박한 일정",
                        "기한이 촉박한 프로젝트를 성공시킨 경험이 있나요?",
                        Set.of("Deadline", "Execution", "Result")
                ),
                new FallbackQuestion(
                        "피드백 수용",
                        "피드백을 받았을 때 수용하고 반영한 사례를 말해 주세요.",
                        Set.of("Feedback", "Growth", "Communication")
                ),
                new FallbackQuestion(
                        "팀을 위한 양보",
                        "팀 성과를 위해 개인이 양보했던 경험이 있나요?",
                        Set.of("Teamwork", "Sacrifice", "Collaboration")
                ),
                new FallbackQuestion(
                        "기여 증명",
                        "성과를 측정할 수 없는 업무에서 기여를 어떻게 증명하나요?",
                        Set.of("Impact", "Measurement", "Performance")
                ),
                new FallbackQuestion(
                        "실패와 재발 방지",
                        "실패 경험을 통해 배운 점과 재발 방지 방법은 무엇인가요?",
                        Set.of("Failure", "Learning", "Improvement")
                ),
                new FallbackQuestion(
                        "리더십 경험",
                        "리더십을 발휘한 경험이 있다면 말해 주세요.",
                        Set.of("Leadership", "Influence", "Team")
                ),
                new FallbackQuestion(
                        "품질 vs 속도",
                        "업무 품질과 속도 사이의 균형을 어떻게 맞추나요?",
                        Set.of("Quality", "Speed", "TradeOff")
                ),
                new FallbackQuestion(
                        "입사 후 목표",
                        "우리 회사(또는 조직)에서 이루고 싶은 목표는 무엇인가요?",
                        Set.of("Goal", "Vision", "Career")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        "불확실성 대응",
                        "불확실성이 큰 과제에서 문제를 정의하고 실행한 과정을 설명해 주세요.",
                        Set.of("Ambiguity", "Execution", "ProblemSolving")
                ),
                new FallbackQuestion(
                        "데이터 vs 직관",
                        "데이터와 직관이 충돌했을 때 어떤 기준으로 결정했나요?",
                        Set.of("DecisionMaking", "Data", "Judgment")
                ),
                new FallbackQuestion(
                        "조직 개선 경험",
                        "조직의 비효율을 발견하고 개선했던 경험을 구체적으로 말해 주세요.",
                        Set.of("Improvement", "Process", "Leadership")
                ),
                new FallbackQuestion(
                        "설득 경험",
                        "이해관계자가 강하게 반대하는 상황에서 설득한 경험이 있나요?",
                        Set.of("Stakeholder", "Persuasion", "Communication")
                ),
                new FallbackQuestion(
                        "부진 상황 전략",
                        "성과가 부진한 상황에서 본인이 선택한 전략과 결과를 설명해 주세요.",
                        Set.of("Strategy", "Turnaround", "Result")
                ),
                new FallbackQuestion(
                        "실패 가능 프로젝트",
                        "팀이 실패할 가능성이 큰 프로젝트를 맡았을 때 접근 방법은 무엇인가요?",
                        Set.of("RiskManagement", "Execution", "Leadership")
                ),
                new FallbackQuestion(
                        "단기 vs 장기 가치",
                        "단기 성과와 장기 가치가 충돌할 때 어떤 기준으로 선택하나요?",
                        Set.of("TradeOff", "LongTerm", "DecisionMaking")
                ),
                new FallbackQuestion(
                        "리스크 방어 사례",
                        "본인의 의사결정이 큰 손실/리스크를 막았던 사례가 있나요?",
                        Set.of("Risk", "Decision", "Impact")
                ),
                new FallbackQuestion(
                        "윤리적 딜레마",
                        "윤리적 딜레마 상황에서 어떻게 판단하고 행동했나요?",
                        Set.of("Ethics", "DecisionMaking", "Responsibility")
                ),
                new FallbackQuestion(
                        "커리어 전략",
                        "향후 3~5년 커리어 전략과 그 근거를 설명해 주세요.",
                        Set.of("CareerPlan", "Vision", "Growth")
                )
        ));

        return Map.copyOf(map);
    }

}
