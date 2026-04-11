package com.mockio.core_service.ai.fallback.question.data;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.core_service.ai.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.generator.question.Question;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DataEngineerFallbackQuestions {

    private DataEngineerFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<FallbackQuestion>> byDifficulty() {
        Map<InterviewDifficulty, List<FallbackQuestion>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                new FallbackQuestion(
                        new Question(
                                "정형 vs 비정형 데이터",
                                "정형 데이터와 비정형 데이터의 차이는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "StructuredData",
                        Set.of("StructuredData", "UnstructuredData", "DataModel")
                ),
                new FallbackQuestion(
                        new Question(
                                "배치 처리 개념",
                                "배치 처리란 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "BatchProcessing",
                        Set.of("BatchProcessing", "DataPipeline", "ETL")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 정합성",
                                "데이터 정합성이 중요한 이유는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Consistency",
                        Set.of("Consistency", "DataQuality", "Integrity")
                ),
                new FallbackQuestion(
                        new Question(
                                "SQL SELECT 기본",
                                "기본적인 SQL SELECT 문을 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "SELECT",
                        Set.of("SQL", "SELECT", "Query")
                ),
                new FallbackQuestion(
                        new Question(
                                "NULL 문제",
                                "NULL 값이 문제되는 이유는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "NULL",
                        Set.of("SQL", "NULL", "DataQuality")
                ),
                new FallbackQuestion(
                        new Question(
                                "ETL 개념",
                                "ETL이란 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "ETL",
                        Set.of("ETL", "DataPipeline", "Transformation")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 품질",
                                "데이터 품질이란 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "DataQuality",
                        Set.of("DataQuality", "Validation", "Integrity")
                ),
                new FallbackQuestion(
                        new Question(
                                "로그 데이터 가치",
                                "로그 데이터는 어떤 가치를 가지나요?",
                                "BASIC",
                                null
                        ),
                        "LogData",
                        Set.of("LogData", "Analytics", "Event")
                ),
                new FallbackQuestion(
                        new Question(
                                "CSV vs JSON",
                                "CSV와 JSON의 차이는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "CSV",
                        Set.of("CSV", "JSON", "DataFormat")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 시각화 목적",
                                "데이터 시각화의 목적은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Visualization",
                        Set.of("Visualization", "Analytics", "BI")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        new Question(
                                "ETL 파이프라인",
                                "ETL 파이프라인을 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "ETL",
                        Set.of("ETL", "Pipeline", "DataEngineering")
                ),
                new FallbackQuestion(
                        new Question(
                                "정규화 vs 비정규화",
                                "정규화와 비정규화의 차이를 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "Normalization",
                        Set.of("Normalization", "Denormalization", "Database")
                ),
                new FallbackQuestion(
                        new Question(
                                "대용량 처리 고려사항",
                                "대용량 데이터 처리 시 고려사항은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "BigData",
                        Set.of("BigData", "Performance", "Scalability")
                ),
                new FallbackQuestion(
                        new Question(
                                "집계 쿼리 성능",
                                "집계 쿼리 성능을 개선하는 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Aggregation",
                        Set.of("Aggregation", "SQL", "Performance")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 파티셔닝",
                                "데이터 파티셔닝의 장점은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Partitioning",
                        Set.of("Partitioning", "Database", "Performance")
                ),
                new FallbackQuestion(
                        new Question(
                                "로그 vs 이벤트",
                                "로그와 이벤트 데이터의 차이는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Streaming",
                        Set.of("Log", "Event", "Streaming")
                ),
                new FallbackQuestion(
                        new Question(
                                "스키마 변경 주의점",
                                "스키마 변경 시 주의할 점은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Migration",
                        Set.of("Schema", "Migration", "DataModel")
                ),
                new FallbackQuestion(
                        new Question(
                                "실시간 처리",
                                "실시간 데이터 처리의 특징은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Streaming",
                        Set.of("Streaming", "RealTime", "DataPipeline")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 검증 시점",
                                "데이터 검증은 언제 수행해야 하나요?",
                                "BASIC",
                                null
                        ),
                        "Validation",
                        Set.of("Validation", "DataQuality", "ETL")
                ),
                new FallbackQuestion(
                        new Question(
                                "인덱스와 분석 쿼리",
                                "인덱스가 분석 쿼리에 미치는 영향은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Index",
                        Set.of("Index", "SQL", "Performance")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        new Question(
                                "Lambda vs Kappa",
                                "Lambda / Kappa 아키텍처의 차이를 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "LambdaArchitecture",
                        Set.of("LambdaArchitecture", "KappaArchitecture", "Streaming")
                ),
                new FallbackQuestion(
                        new Question(
                                "레이크 vs 웨어하우스",
                                "데이터 레이크와 웨어하우스의 차이는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "DataLake",
                        Set.of("DataLake", "DataWarehouse", "Analytics")
                ),
                new FallbackQuestion(
                        new Question(
                                "스트리밍 정확성",
                                "스트리밍 데이터의 정확성을 어떻게 보장하나요?",
                                "BASIC",
                                null
                        ),
                        "ExactlyOnce",
                        Set.of("Streaming", "ExactlyOnce", "Consistency")
                ),
                new FallbackQuestion(
                        new Question(
                                "중복 제거 전략",
                                "데이터 중복을 제거하는 전략은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Deduplication",
                        Set.of("Deduplication", "DataQuality", "ETL")
                ),
                new FallbackQuestion(
                        new Question(
                                "OLTP vs OLAP",
                                "OLTP와 OLAP의 차이는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "OLTP",
                        Set.of("OLTP", "OLAP", "Database")
                ),
                new FallbackQuestion(
                        new Question(
                                "집계 병목 해결",
                                "대규모 집계 쿼리 병목 해결 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Aggregation",
                        Set.of("Aggregation", "Performance", "QueryOptimization")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 거버넌스",
                                "데이터 거버넌스란 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Governance",
                        Set.of("Governance", "DataPolicy", "Compliance")
                ),
                new FallbackQuestion(
                        new Question(
                                "Schema-on-Read",
                                "스키마 온 리드의 장단점은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "SchemaOnRead",
                        Set.of("SchemaOnRead", "DataLake", "Flexibility")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 복구 전략",
                                "장애 발생 시 데이터 복구 전략은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Recovery",
                        Set.of("Backup", "Recovery", "Resilience")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 품질 관리",
                                "데이터 품질을 지속적으로 관리하는 방법은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "DataQuality",
                        Set.of("DataQuality", "Monitoring", "Validation")
                )
        ));

        return Map.copyOf(map);
    }
}