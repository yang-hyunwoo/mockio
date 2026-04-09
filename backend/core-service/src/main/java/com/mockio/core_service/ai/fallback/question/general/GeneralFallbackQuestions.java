package com.mockio.core_service.ai.fallback.question.general;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.core_service.ai.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.generator.question.Question;

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
                        new Question(
                                "자기소개",
                                "본인 소개를 해 주세요."
                        ),
                        new Question(
                                "자기소개 구조화",
                                "본인의 경험을 기반으로 강점과 성과 중심으로 자기소개를 구성한다면 어떤 구조로 설명하시겠습니까?"
                        ),
                        "SelfIntroduction",
                        Set.of("SelfIntroduction", "Communication", "Career")
                ),
                new FallbackQuestion(
                        new Question(
                                "문제 해결 경험",
                                "최근에 해결했던 문제 경험을 말해 주세요."
                        ),
                        new Question(
                                "문제 해결 과정 상세",
                                "문제를 정의하고 해결하기까지 어떤 가설 검증 과정을 거쳤는지 구체적으로 설명해 주세요."
                        ),
                        "ProblemSolving",
                        Set.of("ProblemSolving", "Experience", "Storytelling")
                ),
                new FallbackQuestion(
                        new Question(
                                "강점",
                                "본인의 강점은 무엇인가요?"
                        ),
                        new Question(
                                "강점의 실무 적용",
                                "본인의 강점이 실제 업무에서 어떤 성과로 이어졌는지 사례로 설명해 주세요."
                        ),
                        "Strength",
                        Set.of("Strength", "SelfAwareness", "Career")
                ),
                new FallbackQuestion(
                        new Question(
                                "약점과 보완",
                                "본인의 약점은 무엇이며 어떻게 보완하나요?"
                        ),
                        new Question(
                                "약점 관리 전략",
                                "약점을 단순 보완이 아니라 강점으로 전환하거나 리스크를 줄이기 위해 어떤 전략을 사용하셨나요?"
                        ),
                        "Weakness",
                        Set.of("Weakness", "Improvement", "Growth")
                ),
                new FallbackQuestion(
                        new Question(
                                "협업 가치관",
                                "협업할 때 중요하게 생각하는 것은 무엇인가요?"
                        ),
                        new Question(
                                "협업 충돌 대응",
                                "협업 과정에서 의견 충돌이 발생했을 때 어떤 기준으로 의사결정을 내리시나요?"
                        ),
                        "Collaboration",
                        Set.of("Collaboration", "Teamwork", "Communication")
                ),
                new FallbackQuestion(
                        new Question(
                                "스트레스 관리",
                                "스트레스를 관리하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "고압 상황 대응",
                                "업무 강도가 높은 상황에서 지속적으로 퍼포먼스를 유지하기 위해 어떤 루틴이나 전략을 사용하시나요?"
                        ),
                        "StressManagement",
                        Set.of("StressManagement", "Resilience", "WorkLife")
                ),
                new FallbackQuestion(
                        new Question(
                                "실수와 개선",
                                "실수를 했던 경험과 개선한 방법을 말해 주세요."
                        ),
                        new Question(
                                "재발 방지 체계",
                                "같은 실수가 반복되지 않도록 개인 또는 팀 차원에서 어떤 구조를 만들었나요?"
                        ),
                        "Failure",
                        Set.of("Failure", "Learning", "Improvement")
                ),
                new FallbackQuestion(
                        new Question(
                                "학습 방법",
                                "새로운 것을 학습하는 본인만의 방법은 무엇인가요?"
                        ),
                        new Question(
                                "학습 효율 최적화",
                                "짧은 시간 안에 새로운 기술이나 도메인을 익혀야 할 때 어떤 방식으로 학습 효율을 높이시나요?"
                        ),
                        "Learning",
                        Set.of("Learning", "Growth", "Skill")
                ),
                new FallbackQuestion(
                        new Question(
                                "선호 업무 환경",
                                "원하는 업무 환경/문화는 무엇인가요?"
                        ),
                        new Question(
                                "환경 적응 기준",
                                "이상적인 환경이 아닐 때도 성과를 내기 위해 어떻게 적응하시나요?"
                        ),
                        "Culture",
                        Set.of("Culture", "Environment", "Fit")
                ),
                new FallbackQuestion(
                        new Question(
                                "지원 동기",
                                "지원한 이유는 무엇인가요?"
                        ),
                        new Question(
                                "회사 선택 기준",
                                "회사를 선택할 때 산업, 기술, 조직 문화 중 어떤 기준을 우선순위로 두고 판단하시나요?"
                        ),
                        "Motivation",
                        Set.of("Motivation", "Career", "CompanyFit")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        new Question(
                                "갈등 해결 STAR",
                                "갈등 상황을 해결한 경험을 STAR 방식으로 설명해 주세요."
                        ),
                        new Question(
                                "갈등 해결 기준",
                                "갈등 상황에서 개인 감정보다 문제 해결에 집중하기 위해 어떤 기준으로 판단하셨나요?"
                        ),
                        "STAR",
                        Set.of("Conflict", "STAR", "ProblemSolving")
                ),
                new FallbackQuestion(
                        new Question(
                                "우선순위 충돌",
                                "우선순위가 충돌할 때 의사결정을 어떻게 하나요?"
                        ),
                        new Question(
                                "우선순위 판단 기준",
                                "긴급도와 중요도가 충돌할 때 어떤 기준으로 우선순위를 정하시나요?"
                        ),
                        "Prioritization",
                        Set.of("Prioritization", "DecisionMaking", "Execution")
                ),
                new FallbackQuestion(
                        new Question(
                                "촉박한 일정",
                                "기한이 촉박한 프로젝트를 성공시킨 경험이 있나요?"
                        ),
                        new Question(
                                "일정 압박 대응 전략",
                                "시간이 부족한 상황에서 품질을 유지하면서 일정 준수를 위해 어떤 선택을 하셨나요?"
                        ),
                        "Deadline",
                        Set.of("Deadline", "Execution", "Result")
                ),
                new FallbackQuestion(
                        new Question(
                                "피드백 수용",
                                "피드백을 받았을 때 수용하고 반영한 사례를 말해 주세요."
                        ),
                        new Question(
                                "피드백 판단 기준",
                                "피드백을 무조건 수용하는 것이 아니라 선별적으로 반영해야 할 때 어떤 기준을 사용하시나요?"
                        ),
                        "Feedback",
                        Set.of("Feedback", "Growth", "Communication")
                ),
                new FallbackQuestion(
                        new Question(
                                "팀을 위한 양보",
                                "팀 성과를 위해 개인이 양보했던 경험이 있나요?"
                        ),
                        new Question(
                                "개인 vs 팀 균형",
                                "개인의 성과와 팀의 성과가 충돌할 때 어떤 기준으로 선택하시나요?"
                        ),
                        "Sacrifice",
                        Set.of("Teamwork", "Sacrifice", "Collaboration")
                ),
                new FallbackQuestion(
                        new Question(
                                "기여 증명",
                                "성과를 측정할 수 없는 업무에서 기여를 어떻게 증명하나요?"
                        ),
                        new Question(
                                "정성 기여 측정",
                                "정량 지표가 없는 업무에서도 기여도를 설명하기 위해 어떤 방식으로 성과를 구조화하시나요?"
                        ),
                        "Impact",
                        Set.of("Impact", "Measurement", "Performance")
                ),
                new FallbackQuestion(
                        new Question(
                                "실패와 재발 방지",
                                "실패 경험을 통해 배운 점과 재발 방지 방법은 무엇인가요?"
                        ),
                        new Question(
                                "실패 시스템화",
                                "개인의 실패 경험을 팀 차원의 프로세스 개선으로 확장한 사례가 있나요?"
                        ),
                        "Failure",
                        Set.of("Failure", "Learning", "Improvement")
                ),
                new FallbackQuestion(
                        new Question(
                                "리더십 경험",
                                "리더십을 발휘한 경험이 있다면 말해 주세요."
                        ),
                        new Question(
                                "리더십 스타일",
                                "상황에 따라 리더십 스타일을 어떻게 바꿔야 하는지 사례를 들어 설명해 주세요."
                        ),
                        "Leadership",
                        Set.of("Leadership", "Influence", "Team")
                ),
                new FallbackQuestion(
                        new Question(
                                "품질 vs 속도",
                                "업무 품질과 속도 사이의 균형을 어떻게 맞추나요?"
                        ),
                        new Question(
                                "트레이드오프 판단",
                                "품질을 희생해서라도 속도를 선택해야 하는 상황은 어떤 경우인가요?"
                        ),
                        "TradeOff",
                        Set.of("Quality", "Speed", "TradeOff")
                ),
                new FallbackQuestion(
                        new Question(
                                "입사 후 목표",
                                "우리 회사(또는 조직)에서 이루고 싶은 목표는 무엇인가요?"
                        ),
                        new Question(
                                "목표 구체화",
                                "입사 후 목표를 단기/중기/장기로 나눠 구체적으로 설명해 주세요."
                        ),
                        "Goal",
                        Set.of("Goal", "Vision", "Career")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        new Question(
                                "불확실성 대응",
                                "불확실성이 큰 과제에서 문제를 정의하고 실행한 과정을 설명해 주세요."
                        ),
                        new Question(
                                "불확실성 구조화",
                                "정보가 부족한 상황에서 가설을 세우고 검증하는 과정을 어떻게 설계하셨나요?"
                        ),
                        "Ambiguity",
                        Set.of("Ambiguity", "Execution", "ProblemSolving")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 vs 직관",
                                "데이터와 직관이 충돌했을 때 어떤 기준으로 결정했나요?"
                        ),
                        new Question(
                                "판단 기준 정립",
                                "데이터의 신뢰도가 낮거나 불완전할 때 어떤 방식으로 의사결정을 내리시나요?"
                        ),
                        "Judgment",
                        Set.of("DecisionMaking", "Data", "Judgment")
                ),
                new FallbackQuestion(
                        new Question(
                                "조직 개선 경험",
                                "조직의 비효율을 발견하고 개선했던 경험을 구체적으로 말해 주세요."
                        ),
                        new Question(
                                "조직 변화 확산",
                                "개선 활동을 개인 수준이 아니라 조직 전체로 확산시키기 위해 어떤 전략을 사용하셨나요?"
                        ),
                        "Improvement",
                        Set.of("Improvement", "Process", "Leadership")
                ),
                new FallbackQuestion(
                        new Question(
                                "설득 경험",
                                "이해관계자가 강하게 반대하는 상황에서 설득한 경험이 있나요?"
                        ),
                        new Question(
                                "설득 전략 구조화",
                                "데이터, 논리, 감정 요소를 활용해 설득 전략을 어떻게 구성하셨나요?"
                        ),
                        "Persuasion",
                        Set.of("Stakeholder", "Persuasion", "Communication")
                ),
                new FallbackQuestion(
                        new Question(
                                "부진 상황 전략",
                                "성과가 부진한 상황에서 본인이 선택한 전략과 결과를 설명해 주세요."
                        ),
                        new Question(
                                "턴어라운드 접근",
                                "성과가 낮은 상황에서 원인 분석과 실행 전략을 어떤 순서로 진행하셨나요?"
                        ),
                        "Turnaround",
                        Set.of("Strategy", "Turnaround", "Result")
                ),
                new FallbackQuestion(
                        new Question(
                                "실패 가능 프로젝트",
                                "팀이 실패할 가능성이 큰 프로젝트를 맡았을 때 접근 방법은 무엇인가요?"
                        ),
                        new Question(
                                "리스크 관리 구조",
                                "실패 가능성을 낮추기 위해 어떤 리스크 관리 체계를 설계하셨나요?"
                        ),
                        "RiskManagement",
                        Set.of("RiskManagement", "Execution", "Leadership")
                ),
                new FallbackQuestion(
                        new Question(
                                "단기 vs 장기 가치",
                                "단기 성과와 장기 가치가 충돌할 때 어떤 기준으로 선택하나요?"
                        ),
                        new Question(
                                "의사결정 프레임워크",
                                "단기 KPI와 장기 전략 사이에서 균형을 맞추기 위한 판단 기준은 무엇인가요?"
                        ),
                        "LongTerm",
                        Set.of("TradeOff", "LongTerm", "DecisionMaking")
                ),
                new FallbackQuestion(
                        new Question(
                                "리스크 방어 사례",
                                "본인의 의사결정이 큰 손실/리스크를 막았던 사례가 있나요?"
                        ),
                        new Question(
                                "리스크 감지 방법",
                                "잠재적인 리스크를 사전에 감지하기 위해 어떤 신호나 지표를 활용하셨나요?"
                        ),
                        "Risk",
                        Set.of("Risk", "Decision", "Impact")
                ),
                new FallbackQuestion(
                        new Question(
                                "윤리적 딜레마",
                                "윤리적 딜레마 상황에서 어떻게 판단하고 행동했나요?"
                        ),
                        new Question(
                                "윤리 기준 설정",
                                "조직의 이익과 개인의 윤리 기준이 충돌할 때 어떤 기준으로 의사결정을 내리시나요?"
                        ),
                        "Ethics",
                        Set.of("Ethics", "DecisionMaking", "Responsibility")
                ),
                new FallbackQuestion(
                        new Question(
                                "커리어 전략",
                                "향후 3~5년 커리어 전략과 그 근거를 설명해 주세요."
                        ),
                        new Question(
                                "커리어 실행 계획",
                                "커리어 목표를 달성하기 위해 어떤 역량을 어떤 순서로 개발할 계획인가요?"
                        ),
                        "CareerPlan",
                        Set.of("CareerPlan", "Vision", "Growth")
                )
        ));

        return Map.copyOf(map);
    }
}