package com.mockio.ai_service.fallback.question.hr;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class HrFallbackQuestions {

    private HrFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<String>> byDifficulty() {
        Map<InterviewDifficulty, List<String>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                "HR의 주요 역할은 무엇인가요?",
                "채용 프로세스의 기본 단계는 무엇인가요?",
                "온보딩(Onboarding)이 중요한 이유는 무엇인가요?",
                "평가(Evaluation)와 피드백(Feedback)의 차이는 무엇인가요?",
                "조직 문화란 무엇인가요?",
                "직무 기술서(JD)는 왜 필요한가요?",
                "인재상은 왜 정의하나요?",
                "구성원 커뮤니케이션에서 중요한 점은 무엇인가요?",
                "HR 데이터는 어떤 것들이 있나요?",
                "공정성이 중요한 이유는 무엇인가요?"
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                "채용에서 구조화 면접(Structured Interview)의 장점은 무엇인가요?",
                "평가 제도를 설계할 때 고려해야 할 요소는 무엇인가요?",
                "성과 관리(Performance Management) 프로세스를 설명해 주세요.",
                "리텐션(이직 방지)을 개선하기 위한 접근은 무엇인가요?",
                "보상(Compensation)과 복리후생(Benefits) 설계의 핵심은 무엇인가요?",
                "조직 진단(engagement survey)을 어떻게 설계하고 활용하나요?",
                "갈등이 발생했을 때 HR의 개입 원칙은 무엇인가요?",
                "교육/러닝 프로그램의 효과를 측정하는 방법은 무엇인가요?",
                "핵심 인재(HiPo)를 정의하고 육성하는 방법은 무엇인가요?",
                "법적/윤리적 이슈가 있는 인사 사안을 처리할 때 주의점은?"
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                "급격한 조직 성장 시 HR 프로세스를 스케일업하는 방법은 무엇인가요?",
                "평가/보상 체계가 조직 문화를 왜곡할 때 어떻게 개선하나요?",
                "조직 개편(리오그) 시 리스크와 커뮤니케이션 전략은 무엇인가요?",
                "리더십 역량 모델을 만들고 적용하는 방법을 설명해 주세요.",
                "성과가 낮은 팀/구성원에 대한 개선(Performance Improvement) 프로세스는?",
                "다양성과 포용(D&I)을 실질 성과로 연결하는 방법은 무엇인가요?",
                "인재 확보 경쟁이 심한 시장에서 채용 브랜드를 강화하는 방법은?",
                "노무/법적 리스크가 큰 사안에서 의사결정과 기록은 어떻게 관리하나요?",
                "구성원 경험(Employee Experience)을 데이터로 개선하는 방법은?",
                "조직 신뢰가 깨진 상황에서 회복 전략을 어떻게 수립하나요?"
        ));

        return Map.copyOf(map);
    }

}
