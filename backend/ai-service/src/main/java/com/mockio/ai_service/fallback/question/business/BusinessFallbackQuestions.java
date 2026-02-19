package com.mockio.ai_service.fallback.question.business;


import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class BusinessFallbackQuestions {

    private BusinessFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<String>> byDifficulty() {
        Map<InterviewDifficulty, List<String>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                "사업 전략이란 무엇인가요?",
                "시장 분석이 중요한 이유는 무엇인가요?",
                "경쟁사 분석은 어떻게 하나요?",
                "수익 모델의 종류에는 어떤 것들이 있나요?",
                "손익(P&L)의 기본 구성요소는 무엇인가요?",
                "고객 세그먼트란 무엇인가요?",
                "가치 제안(Value Proposition)이란 무엇인가요?",
                "KPI란 무엇인가요?",
                "프로젝트와 전략의 차이는 무엇인가요?",
                "의사결정에 데이터가 중요한 이유는 무엇인가요?"
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                "SWOT 분석을 실제 의사결정에 어떻게 연결하나요?",
                "시장 규모(TAM/SAM/SOM)를 추정하는 방법을 설명해 주세요.",
                "신규 사업 기회를 평가하는 프레임워크를 설명해 주세요.",
                "가격 정책을 설계할 때 고려해야 할 요소는 무엇인가요?",
                "유닛 이코노믹스(Unit Economics)란 무엇이고 왜 중요한가요?",
                "CAC와 LTV의 의미와 활용을 설명해 주세요.",
                "파트너십/제휴 전략을 수립하는 방법은 무엇인가요?",
                "OKR을 수립하고 리뷰하는 프로세스를 설명해 주세요.",
                "경쟁 우위(모트)를 정의하고 강화하는 방법은 무엇인가요?",
                "성과가 부진한 사업의 턴어라운드 접근법은 무엇인가요?"
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                "성장률은 높은데 수익성이 악화되는 상황에서 전략은 어떻게 조정하나요?",
                "규제/정책 변화가 큰 시장에서 리스크를 반영한 전략 수립 방법은?",
                "멀티 프로덕트/멀티 마켓에서 자원 배분을 최적화하는 방법은?",
                "데이터가 상충할 때(지표 간 트레이드오프) 의사결정을 어떻게 하나요?",
                "신규 시장 진입(Go-to-Market) 전략을 단계별로 설명해 주세요.",
                "M&A/투자 검토 시 핵심 체크포인트(재무/시너지/리스크)는 무엇인가요?",
                "플랫폼 비즈니스에서 네트워크 효과를 측정하고 강화하는 방법은?",
                "B2B와 B2C 전략 수립의 차이를 설명해 주세요.",
                "가격 인상(또는 인하) 실험을 설계하고 리스크를 통제하는 방법은?",
                "조직 내 저항이 큰 전략 과제를 실행으로 옮기는 방법은 무엇인가요?"
        ));

        return Map.copyOf(map);
    }

}
