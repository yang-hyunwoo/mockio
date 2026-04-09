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
                                "정형 데이터와 비정형 데이터의 차이는 무엇인가요?"
                        ),
                        new Question(
                                "데이터 모델링 영향",
                                "정형 데이터와 비정형 데이터의 차이가 저장 방식과 분석 방식에 어떤 영향을 주는지 설명해 주세요."
                        ),
                        "StructuredData",
                        Set.of("StructuredData", "UnstructuredData", "DataModel")
                ),
                new FallbackQuestion(
                        new Question(
                                "배치 처리 개념",
                                "배치 처리란 무엇인가요?"
                        ),
                        new Question(
                                "배치 처리 활용 사례",
                                "배치 처리가 실시간 처리보다 적합한 대표적인 데이터 업무 사례를 설명해 주세요."
                        ),
                        "BatchProcessing",
                        Set.of("BatchProcessing", "DataPipeline", "ETL")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 정합성",
                                "데이터 정합성이 중요한 이유는 무엇인가요?"
                        ),
                        new Question(
                                "정합성 문제의 실무 영향",
                                "데이터 정합성이 깨졌을 때 리포트, 의사결정, 서비스 운영에 어떤 문제가 발생할 수 있나요?"
                        ),
                        "Consistency",
                        Set.of("Consistency", "DataQuality", "Integrity")
                ),
                new FallbackQuestion(
                        new Question(
                                "SQL SELECT 기본",
                                "기본적인 SQL SELECT 문을 설명해 주세요."
                        ),
                        new Question(
                                "SELECT 작성 실무 포인트",
                                "실무에서 SELECT 문을 작성할 때 필요한 컬럼만 조회하는 것이 중요한 이유는 무엇인가요?"
                        ),
                        "SELECT",
                        Set.of("SQL", "SELECT", "Query")
                ),
                new FallbackQuestion(
                        new Question(
                                "NULL 문제",
                                "NULL 값이 문제되는 이유는 무엇인가요?"
                        ),
                        new Question(
                                "NULL 처리 주의사항",
                                "집계 함수나 조건 비교에서 NULL이 예상치 못한 결과를 만드는 이유를 설명해 주세요."
                        ),
                        "NULL",
                        Set.of("SQL", "NULL", "DataQuality")
                ),
                new FallbackQuestion(
                        new Question(
                                "ETL 개념",
                                "ETL이란 무엇인가요?"
                        ),
                        new Question(
                                "ETL 단계별 역할",
                                "Extract, Transform, Load 각 단계에서 주로 어떤 작업이 수행되는지 설명해 주세요."
                        ),
                        "ETL",
                        Set.of("ETL", "DataPipeline", "Transformation")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 품질",
                                "데이터 품질이란 무엇인가요?"
                        ),
                        new Question(
                                "품질 판단 기준",
                                "데이터 품질을 정확성, 완전성, 일관성 관점에서 어떻게 평가할 수 있나요?"
                        ),
                        "DataQuality",
                        Set.of("DataQuality", "Validation", "Integrity")
                ),
                new FallbackQuestion(
                        new Question(
                                "로그 데이터 가치",
                                "로그 데이터는 어떤 가치를 가지나요?"
                        ),
                        new Question(
                                "로그 데이터 활용 방식",
                                "서비스 로그 데이터를 분석해 사용자 행동이나 장애 원인을 파악하는 과정을 설명해 주세요."
                        ),
                        "LogData",
                        Set.of("LogData", "Analytics", "Event")
                ),
                new FallbackQuestion(
                        new Question(
                                "CSV vs JSON",
                                "CSV와 JSON의 차이는 무엇인가요?"
                        ),
                        new Question(
                                "포맷 선택 기준",
                                "데이터 교환이나 저장 시 CSV와 JSON 중 어떤 형식을 선택할지 판단 기준을 설명해 주세요."
                        ),
                        "CSV",
                        Set.of("CSV", "JSON", "DataFormat")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 시각화 목적",
                                "데이터 시각화의 목적은 무엇인가요?"
                        ),
                        new Question(
                                "좋은 시각화 조건",
                                "좋은 데이터 시각화가 단순 예쁜 차트가 아니라 의사결정을 돕기 위해 가져야 할 조건은 무엇인가요?"
                        ),
                        "Visualization",
                        Set.of("Visualization", "Analytics", "BI")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        new Question(
                                "ETL 파이프라인",
                                "ETL 파이프라인을 설명해 주세요."
                        ),
                        new Question(
                                "ETL 장애 포인트",
                                "ETL 파이프라인 운영 시 데이터 누락, 중복, 지연이 발생하는 주요 원인과 대응 방식을 설명해 주세요."
                        ),
                        "ETL",
                        Set.of("ETL", "Pipeline", "DataEngineering")
                ),
                new FallbackQuestion(
                        new Question(
                                "정규화 vs 비정규화",
                                "정규화와 비정규화의 차이를 설명해 주세요."
                        ),
                        new Question(
                                "모델링 선택 기준",
                                "분석 시스템과 서비스 시스템에서 정규화와 비정규화의 선택 기준이 왜 달라질 수 있는지 설명해 주세요."
                        ),
                        "Normalization",
                        Set.of("Normalization", "Denormalization", "Database")
                ),
                new FallbackQuestion(
                        new Question(
                                "대용량 처리 고려사항",
                                "대용량 데이터 처리 시 고려사항은 무엇인가요?"
                        ),
                        new Question(
                                "확장성 설계 포인트",
                                "대용량 데이터 처리 환경에서 성능, 비용, 안정성의 균형을 어떻게 맞춰야 하나요?"
                        ),
                        "BigData",
                        Set.of("BigData", "Performance", "Scalability")
                ),
                new FallbackQuestion(
                        new Question(
                                "집계 쿼리 성능",
                                "집계 쿼리 성능을 개선하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "집계 최적화 전략",
                                "대규모 GROUP BY, JOIN이 포함된 집계 쿼리에서 병목을 줄이기 위한 방법을 설명해 주세요."
                        ),
                        "Aggregation",
                        Set.of("Aggregation", "SQL", "Performance")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 파티셔닝",
                                "데이터 파티셔닝의 장점은 무엇인가요?"
                        ),
                        new Question(
                                "파티셔닝 설계 기준",
                                "날짜, 고객, 지역 기준 등 어떤 키로 파티셔닝할지 결정할 때 어떤 점을 고려해야 하나요?"
                        ),
                        "Partitioning",
                        Set.of("Partitioning", "Database", "Performance")
                ),
                new FallbackQuestion(
                        new Question(
                                "로그 vs 이벤트",
                                "로그와 이벤트 데이터의 차이는 무엇인가요?"
                        ),
                        new Question(
                                "이벤트 모델링 주의점",
                                "로그와 이벤트를 구분하지 않고 수집했을 때 분석이나 스트리밍 처리에서 어떤 문제가 생길 수 있나요?"
                        ),
                        "Streaming",
                        Set.of("Log", "Event", "Streaming")
                ),
                new FallbackQuestion(
                        new Question(
                                "스키마 변경 주의점",
                                "스키마 변경 시 주의할 점은 무엇인가요?"
                        ),
                        new Question(
                                "하위 호환성 유지",
                                "운영 중인 데이터 파이프라인에서 스키마 변경 시 하위 호환성을 유지하려면 어떤 전략이 필요할까요?"
                        ),
                        "Migration",
                        Set.of("Schema", "Migration", "DataModel")
                ),
                new FallbackQuestion(
                        new Question(
                                "실시간 처리",
                                "실시간 데이터 처리의 특징은 무엇인가요?"
                        ),
                        new Question(
                                "실시간 처리의 어려움",
                                "실시간 처리에서 지연 시간, 순서 보장, 중복 처리 문제가 왜 중요한지 설명해 주세요."
                        ),
                        "Streaming",
                        Set.of("Streaming", "RealTime", "DataPipeline")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 검증 시점",
                                "데이터 검증은 언제 수행해야 하나요?"
                        ),
                        new Question(
                                "다단계 검증 전략",
                                "수집, 변환, 적재 단계 각각에서 데이터 검증을 분리해서 수행해야 하는 이유는 무엇인가요?"
                        ),
                        "Validation",
                        Set.of("Validation", "DataQuality", "ETL")
                ),
                new FallbackQuestion(
                        new Question(
                                "인덱스와 분석 쿼리",
                                "인덱스가 분석 쿼리에 미치는 영향은 무엇인가요?"
                        ),
                        new Question(
                                "인덱스의 한계",
                                "OLTP 환경에서 유용한 인덱스가 대규모 분석 쿼리에서는 기대만큼 효과적이지 않을 수 있는 이유를 설명해 주세요."
                        ),
                        "Index",
                        Set.of("Index", "SQL", "Performance")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        new Question(
                                "Lambda vs Kappa",
                                "Lambda / Kappa 아키텍처의 차이를 설명해 주세요."
                        ),
                        new Question(
                                "아키텍처 선택 기준",
                                "Lambda와 Kappa 아키텍처 중 하나를 선택할 때 운영 복잡성, 일관성, 유지보수 관점에서 어떤 기준을 적용하시겠습니까?"
                        ),
                        "LambdaArchitecture",
                        Set.of("LambdaArchitecture", "KappaArchitecture", "Streaming")
                ),
                new FallbackQuestion(
                        new Question(
                                "레이크 vs 웨어하우스",
                                "데이터 레이크와 웨어하우스의 차이는 무엇인가요?"
                        ),
                        new Question(
                                "저장소 역할 분리",
                                "데이터 레이크와 웨어하우스를 함께 운영할 때 각각 어떤 데이터와 업무를 배치하는 것이 적절한가요?"
                        ),
                        "DataLake",
                        Set.of("DataLake", "DataWarehouse", "Analytics")
                ),
                new FallbackQuestion(
                        new Question(
                                "스트리밍 정확성",
                                "스트리밍 데이터의 정확성을 어떻게 보장하나요?"
                        ),
                        new Question(
                                "Exactly-once 구현 현실",
                                "스트리밍 시스템에서 exactly-once 보장을 구현할 때 기술적 제약과 비용은 무엇인가요?"
                        ),
                        "ExactlyOnce",
                        Set.of("Streaming", "ExactlyOnce", "Consistency")
                ),
                new FallbackQuestion(
                        new Question(
                                "중복 제거 전략",
                                "데이터 중복을 제거하는 전략은 무엇인가요?"
                        ),
                        new Question(
                                "중복 제거 기준 설계",
                                "실시간/배치 환경에서 중복 이벤트를 식별하기 위한 키와 윈도우를 어떻게 설계해야 하나요?"
                        ),
                        "Deduplication",
                        Set.of("Deduplication", "DataQuality", "ETL")
                ),
                new FallbackQuestion(
                        new Question(
                                "OLTP vs OLAP",
                                "OLTP와 OLAP의 차이는 무엇인가요?"
                        ),
                        new Question(
                                "시스템 분리 이유",
                                "트랜잭션 처리 시스템과 분석 시스템을 분리해야 하는 이유를 성능과 모델링 관점에서 설명해 주세요."
                        ),
                        "OLTP",
                        Set.of("OLTP", "OLAP", "Database")
                ),
                new FallbackQuestion(
                        new Question(
                                "집계 병목 해결",
                                "대규모 집계 쿼리 병목 해결 방법은 무엇인가요?"
                        ),
                        new Question(
                                "사전 집계와 분산 처리",
                                "대규모 집계 병목을 줄이기 위해 사전 집계, 파티셔닝, 분산 처리 중 어떤 전략을 우선 적용할지 설명해 주세요."
                        ),
                        "Aggregation",
                        Set.of("Aggregation", "Performance", "QueryOptimization")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 거버넌스",
                                "데이터 거버넌스란 무엇인가요?"
                        ),
                        new Question(
                                "거버넌스 운영 요소",
                                "데이터 거버넌스를 실제 조직에 정착시키기 위해 정책, 권한, 품질 기준을 어떻게 운영해야 하나요?"
                        ),
                        "Governance",
                        Set.of("Governance", "DataPolicy", "Compliance")
                ),
                new FallbackQuestion(
                        new Question(
                                "Schema-on-Read",
                                "스키마 온 리드의 장단점은 무엇인가요?"
                        ),
                        new Question(
                                "유연성과 품질 trade-off",
                                "Schema-on-Read 방식이 유연성을 주는 대신 데이터 품질과 해석 일관성 측면에서 어떤 문제를 만들 수 있나요?"
                        ),
                        "SchemaOnRead",
                        Set.of("SchemaOnRead", "DataLake", "Flexibility")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 복구 전략",
                                "장애 발생 시 데이터 복구 전략은 무엇인가요?"
                        ),
                        new Question(
                                "복구 전략 우선순위",
                                "백업, 재처리, 체크포인트, 이중화 중 어떤 복구 전략을 선택할지 RPO/RTO 기준으로 설명해 주세요."
                        ),
                        "Recovery",
                        Set.of("Backup", "Recovery", "Resilience")
                ),
                new FallbackQuestion(
                        new Question(
                                "데이터 품질 관리",
                                "데이터 품질을 지속적으로 관리하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "품질 모니터링 체계",
                                "데이터 품질 문제를 사후 대응이 아니라 상시 모니터링 체계로 운영하려면 어떤 지표와 프로세스가 필요할까요?"
                        ),
                        "DataQuality",
                        Set.of("DataQuality", "Monitoring", "Validation")
                )
        ));

        return Map.copyOf(map);
    }
}