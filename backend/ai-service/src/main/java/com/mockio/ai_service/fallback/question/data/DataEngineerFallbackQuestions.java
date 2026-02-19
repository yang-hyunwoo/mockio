package com.mockio.ai_service.fallback.question.data;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class DataEngineerFallbackQuestions {

    private DataEngineerFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<String>> byDifficulty() {
        Map<InterviewDifficulty, List<String>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                "정형 데이터와 비정형 데이터의 차이는 무엇인가요?",
                "배치 처리란 무엇인가요?",
                "데이터 정합성이 중요한 이유는 무엇인가요?",
                "기본적인 SQL SELECT 문을 설명해 주세요.",
                "NULL 값이 문제되는 이유는 무엇인가요?",
                "ETL이란 무엇인가요?",
                "데이터 품질이란 무엇인가요?",
                "로그 데이터는 어떤 가치를 가지나요?",
                "CSV와 JSON의 차이는 무엇인가요?",
                "데이터 시각화의 목적은 무엇인가요?"
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                "ETL 파이프라인을 설명해 주세요.",
                "정규화와 비정규화의 차이를 설명해 주세요.",
                "대용량 데이터 처리 시 고려사항은 무엇인가요?",
                "집계 쿼리 성능을 개선하는 방법은 무엇인가요?",
                "데이터 파티셔닝의 장점은 무엇인가요?",
                "로그와 이벤트 데이터의 차이는 무엇인가요?",
                "스키마 변경 시 주의할 점은 무엇인가요?",
                "실시간 데이터 처리의 특징은 무엇인가요?",
                "데이터 검증은 언제 수행해야 하나요?",
                "인덱스가 분석 쿼리에 미치는 영향은 무엇인가요?"
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                "Lambda / Kappa 아키텍처의 차이를 설명해 주세요.",
                "데이터 레이크와 웨어하우스의 차이는 무엇인가요?",
                "스트리밍 데이터의 정확성을 어떻게 보장하나요?",
                "데이터 중복을 제거하는 전략은 무엇인가요?",
                "OLTP와 OLAP의 차이는 무엇인가요?",
                "대규모 집계 쿼리 병목 해결 방법은 무엇인가요?",
                "데이터 거버넌스란 무엇인가요?",
                "스키마 온 리드의 장단점은 무엇인가요?",
                "장애 발생 시 데이터 복구 전략은 무엇인가요?",
                "데이터 품질을 지속적으로 관리하는 방법은 무엇인가요?"
        ));

        return Map.copyOf(map);
    }

}
