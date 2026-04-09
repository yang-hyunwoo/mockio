package com.mockio.core_service.ai.fallback.question.hr;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.core_service.ai.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.generator.question.Question;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class HrFallbackQuestions {

    private HrFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<FallbackQuestion>> byDifficulty() {
        Map<InterviewDifficulty, List<FallbackQuestion>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                new FallbackQuestion(
                        new Question(
                                "HR 역할",
                                "HR의 주요 역할은 무엇인가요?"
                        ),
                        new Question(
                                "HR 역할 확장",
                                "HR이 단순 지원 조직을 넘어 전략적 파트너로 기능하기 위해 필요한 역할은 무엇인가요?"
                        ),
                        "HR",
                        Set.of("HR", "Role", "PeopleManagement")
                ),
                new FallbackQuestion(
                        new Question(
                                "채용 프로세스",
                                "채용 프로세스의 기본 단계는 무엇인가요?"
                        ),
                        new Question(
                                "채용 최적화",
                                "채용 프로세스를 개선해 우수 인재 확보율을 높이기 위한 방법은 무엇인가요?"
                        ),
                        "HiringProcess",
                        Set.of("Recruiting", "HiringProcess", "HR")
                ),
                new FallbackQuestion(
                        new Question(
                                "온보딩 중요성",
                                "온보딩(Onboarding)이 중요한 이유는 무엇인가요?"
                        ),
                        new Question(
                                "온보딩 설계",
                                "신입 구성원이 빠르게 적응하도록 온보딩 프로그램을 어떻게 설계해야 하나요?"
                        ),
                        "Onboarding",
                        Set.of("Onboarding", "Retention", "EmployeeExperience")
                ),
                new FallbackQuestion(
                        new Question(
                                "평가 vs 피드백",
                                "평가(Evaluation)와 피드백(Feedback)의 차이는 무엇인가요?"
                        ),
                        new Question(
                                "평가 제도 개선",
                                "평가가 형식적으로 흐르지 않도록 피드백 중심으로 개선하려면 어떻게 해야 하나요?"
                        ),
                        "Evaluation",
                        Set.of("Evaluation", "Feedback", "Performance")
                ),
                new FallbackQuestion(
                        new Question(
                                "조직 문화",
                                "조직 문화란 무엇인가요?"
                        ),
                        new Question(
                                "문화 형성 요소",
                                "조직 문화가 자연스럽게 형성되는 것이 아니라 관리되어야 하는 이유는 무엇인가요?"
                        ),
                        "Culture",
                        Set.of("Culture", "Organization", "Leadership")
                ),
                new FallbackQuestion(
                        new Question(
                                "직무 기술서",
                                "직무 기술서(JD)는 왜 필요한가요?"
                        ),
                        new Question(
                                "JD 품질 기준",
                                "좋은 JD가 지원자 경험과 채용 효율에 어떤 영향을 주는지 설명해 주세요."
                        ),
                        "JD",
                        Set.of("JD", "Recruiting", "RoleDefinition")
                ),
                new FallbackQuestion(
                        new Question(
                                "인재상 정의",
                                "인재상은 왜 정의하나요?"
                        ),
                        new Question(
                                "인재상 활용",
                                "인재상을 채용, 평가, 조직 문화에 일관되게 적용하려면 어떤 방식이 필요할까요?"
                        ),
                        "Talent",
                        Set.of("Talent", "CultureFit", "Recruiting")
                ),
                new FallbackQuestion(
                        new Question(
                                "커뮤니케이션",
                                "구성원 커뮤니케이션에서 중요한 점은 무엇인가요?"
                        ),
                        new Question(
                                "조직 커뮤니케이션 설계",
                                "조직 내 정보 전달이 왜곡되지 않도록 커뮤니케이션 구조를 어떻게 설계해야 하나요?"
                        ),
                        "Communication",
                        Set.of("Communication", "Engagement", "HR")
                ),
                new FallbackQuestion(
                        new Question(
                                "HR 데이터",
                                "HR 데이터는 어떤 것들이 있나요?"
                        ),
                        new Question(
                                "HR 데이터 활용",
                                "HR 데이터를 활용해 조직 문제를 진단하고 개선하는 방법을 설명해 주세요."
                        ),
                        "HRAnalytics",
                        Set.of("HRAnalytics", "Data", "Metrics")
                ),
                new FallbackQuestion(
                        new Question(
                                "공정성",
                                "공정성이 중요한 이유는 무엇인가요?"
                        ),
                        new Question(
                                "공정성 확보 방법",
                                "채용, 평가, 보상 과정에서 공정성을 확보하기 위한 구체적인 방법은 무엇인가요?"
                        ),
                        "Fairness",
                        Set.of("Fairness", "Compliance", "Ethics")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        new Question(
                                "구조화 면접",
                                "채용에서 구조화 면접(Structured Interview)의 장점은 무엇인가요?"
                        ),
                        new Question(
                                "구조화 면접 설계",
                                "구조화 면접 질문을 설계할 때 직무 적합성과 조직 적합성을 어떻게 평가할 수 있나요?"
                        ),
                        "StructuredInterview",
                        Set.of("StructuredInterview", "Recruiting", "Evaluation")
                ),
                new FallbackQuestion(
                        new Question(
                                "평가 제도 설계",
                                "평가 제도를 설계할 때 고려해야 할 요소는 무엇인가요?"
                        ),
                        new Question(
                                "평가 제도 실패 원인",
                                "평가 제도가 조직 내 불신을 초래하는 주요 원인과 개선 방법은 무엇인가요?"
                        ),
                        "PerformanceReview",
                        Set.of("PerformanceReview", "Evaluation", "HRPolicy")
                ),
                new FallbackQuestion(
                        new Question(
                                "성과 관리",
                                "성과 관리(Performance Management) 프로세스를 설명해 주세요."
                        ),
                        new Question(
                                "성과 관리 실효성",
                                "성과 관리가 단순 평가가 아니라 지속적인 성과 향상으로 이어지게 하려면 어떻게 해야 하나요?"
                        ),
                        "PerformanceManagement",
                        Set.of("PerformanceManagement", "KPI", "Review")
                ),
                new FallbackQuestion(
                        new Question(
                                "리텐션 전략",
                                "리텐션(이직 방지)을 개선하기 위한 접근은 무엇인가요?"
                        ),
                        new Question(
                                "리텐션 우선순위",
                                "보상, 문화, 성장 기회 중 어떤 요소를 먼저 개선해야 하는지 판단 기준은 무엇인가요?"
                        ),
                        "Retention",
                        Set.of("Retention", "Engagement", "HRStrategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "보상 설계",
                                "보상(Compensation)과 복리후생(Benefits) 설계의 핵심은 무엇인가요?"
                        ),
                        new Question(
                                "보상 공정성 문제",
                                "보상 체계가 내부 불만을 유발하지 않도록 설계하려면 어떤 기준이 필요할까요?"
                        ),
                        "Compensation",
                        Set.of("Compensation", "Benefits", "Reward")
                ),
                new FallbackQuestion(
                        new Question(
                                "조직 진단",
                                "조직 진단(engagement survey)을 어떻게 설계하고 활용하나요?"
                        ),
                        new Question(
                                "조직 진단 활용",
                                "설문 결과를 단순 수집이 아니라 실제 조직 개선으로 연결하는 방법은 무엇인가요?"
                        ),
                        "Survey",
                        Set.of("Engagement", "Survey", "HRAnalytics")
                ),
                new FallbackQuestion(
                        new Question(
                                "갈등 개입 원칙",
                                "갈등이 발생했을 때 HR의 개입 원칙은 무엇인가요?"
                        ),
                        new Question(
                                "갈등 해결 전략",
                                "조직 갈등을 중재할 때 중립성과 문제 해결 사이에서 균형을 어떻게 잡으시나요?"
                        ),
                        "ConflictManagement",
                        Set.of("ConflictManagement", "Mediation", "HR")
                ),
                new FallbackQuestion(
                        new Question(
                                "교육 효과 측정",
                                "교육/러닝 프로그램의 효과를 측정하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "교육 ROI 분석",
                                "교육 프로그램이 실제 성과 향상으로 이어졌는지 판단하는 기준은 무엇인가요?"
                        ),
                        "ROI",
                        Set.of("Learning", "Training", "ROI")
                ),
                new FallbackQuestion(
                        new Question(
                                "핵심 인재 육성",
                                "핵심 인재(HiPo)를 정의하고 육성하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "HiPo 리스크 관리",
                                "핵심 인재 중심 정책이 조직 내 불균형을 만들지 않도록 어떻게 관리해야 하나요?"
                        ),
                        "HiPo",
                        Set.of("HiPo", "TalentDevelopment", "Leadership")
                ),
                new FallbackQuestion(
                        new Question(
                                "법적/윤리적 인사 이슈",
                                "법적/윤리적 이슈가 있는 인사 사안을 처리할 때 주의점은 무엇인가요?"
                        ),
                        new Question(
                                "리스크 대응 전략",
                                "노무 리스크가 있는 상황에서 법적 대응과 조직 신뢰 유지 사이의 균형은 어떻게 잡아야 하나요?"
                        ),
                        "LaborLaw",
                        Set.of("Compliance", "LaborLaw", "Ethics")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        new Question(
                                "HR 스케일업",
                                "급격한 조직 성장 시 HR 프로세스를 스케일업하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "스케일업 리스크",
                                "빠른 성장 과정에서 HR 프로세스가 무너지지 않도록 어떤 구조를 먼저 잡아야 하나요?"
                        ),
                        "Scaling",
                        Set.of("Scaling", "HRProcess", "Growth")
                ),
                new FallbackQuestion(
                        new Question(
                                "보상 체계 왜곡",
                                "평가/보상 체계가 조직 문화를 왜곡할 때 어떻게 개선하나요?"
                        ),
                        new Question(
                                "인센티브 설계",
                                "잘못된 인센티브 구조가 조직 행동을 왜곡하는 사례와 이를 방지하는 방법은 무엇인가요?"
                        ),
                        "Incentive",
                        Set.of("Compensation", "Culture", "Incentive")
                ),
                new FallbackQuestion(
                        new Question(
                                "조직 개편 전략",
                                "조직 개편(리오그) 시 리스크와 커뮤니케이션 전략은 무엇인가요?"
                        ),
                        new Question(
                                "리오그 실패 원인",
                                "조직 개편이 실패하는 주요 원인과 이를 방지하기 위한 커뮤니케이션 전략은 무엇인가요?"
                        ),
                        "Reorg",
                        Set.of("Reorg", "ChangeManagement", "Communication")
                ),
                new FallbackQuestion(
                        new Question(
                                "리더십 모델",
                                "리더십 역량 모델을 만들고 적용하는 방법을 설명해 주세요."
                        ),
                        new Question(
                                "리더십 평가 기준",
                                "리더십 역량을 정량/정성적으로 평가하기 위한 기준은 어떻게 설정하나요?"
                        ),
                        "CompetencyModel",
                        Set.of("Leadership", "CompetencyModel", "HRStrategy")
                ),
                new FallbackQuestion(
                        new Question(
                                "성과 개선 프로세스",
                                "성과가 낮은 팀/구성원에 대한 개선(Performance Improvement) 프로세스는 무엇인가요?"
                        ),
                        new Question(
                                "PIP 운영 리스크",
                                "PIP가 단순 퇴출 프로세스로 인식되지 않도록 운영하려면 어떻게 해야 하나요?"
                        ),
                        "PIP",
                        Set.of("PIP", "Performance", "HR")
                ),
                new FallbackQuestion(
                        new Question(
                                "D&I 전략",
                                "다양성과 포용(D&I)을 실질 성과로 연결하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "D&I 성과 측정",
                                "D&I 정책이 실제 조직 성과에 기여하는지 어떻게 측정할 수 있나요?"
                        ),
                        "Diversity",
                        Set.of("Diversity", "Inclusion", "Culture")
                ),
                new FallbackQuestion(
                        new Question(
                                "채용 브랜딩",
                                "인재 확보 경쟁이 심한 시장에서 채용 브랜드를 강화하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "브랜딩 전략 실행",
                                "채용 브랜딩을 단순 홍보가 아니라 실제 지원자 경험으로 연결하는 방법은 무엇인가요?"
                        ),
                        "EmployerBranding",
                        Set.of("EmployerBranding", "Recruiting", "Talent")
                ),
                new FallbackQuestion(
                        new Question(
                                "노무 리스크 관리",
                                "노무/법적 리스크가 큰 사안에서 의사결정과 기록은 어떻게 관리하나요?"
                        ),
                        new Question(
                                "리스크 대응 체계",
                                "법적 분쟁 가능성이 있는 상황에서 HR이 사전에 준비해야 할 대응 체계는 무엇인가요?"
                        ),
                        "RiskManagement",
                        Set.of("LaborLaw", "RiskManagement", "Compliance")
                ),
                new FallbackQuestion(
                        new Question(
                                "Employee Experience 개선",
                                "구성원 경험(Employee Experience)을 데이터로 개선하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "EX 지표 설계",
                                "구성원 경험을 측정하기 위한 핵심 지표와 개선 프로세스를 어떻게 설계하시나요?"
                        ),
                        "EmployeeExperience",
                        Set.of("EmployeeExperience", "HRAnalytics", "Engagement")
                ),
                new FallbackQuestion(
                        new Question(
                                "신뢰 회복 전략",
                                "조직 신뢰가 깨진 상황에서 회복 전략을 어떻게 수립하나요?"
                        ),
                        new Question(
                                "신뢰 회복 실행",
                                "신뢰 회복을 위해 단기 행동과 장기 구조 개선을 어떻게 병행하시겠습니까?"
                        ),
                        "Trust",
                        Set.of("Trust", "Leadership", "Culture")
                )
        ));

        return Map.copyOf(map);
    }
}