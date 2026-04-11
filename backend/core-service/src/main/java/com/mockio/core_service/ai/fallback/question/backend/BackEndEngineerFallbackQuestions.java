package com.mockio.core_service.ai.fallback.question.backend;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.generator.question.Question;
import com.mockio.core_service.ai.fallback.FallbackQuestion;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BackEndEngineerFallbackQuestions {

    private BackEndEngineerFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<FallbackQuestion>> byDifficulty() {
        Map<InterviewDifficulty, List<FallbackQuestion>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                new FallbackQuestion(
                        new Question(
                                "REST API 개념",
                                "REST API란 무엇이며, RESTful 하다는 것은 어떤 의미인가요?",
                                "BASIC",
                                null
                        ),
                        "REST",
                        Set.of("REST", "HTTP", "API")
                ),
                new FallbackQuestion(
                        new Question(
                                "HTTP 메서드",
                                "HTTP 메서드(GET, POST, PUT, DELETE)의 차이를 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "Method",
                        Set.of("HTTP", "REST", "Method")
                ),
                new FallbackQuestion(
                        new Question(
                                "HTTP 상태 코드",
                                "상태 코드 200, 400, 401, 403, 500의 의미를 각각 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "StatusCode",
                        Set.of("HTTP", "StatusCode", "REST")
                ),
                new FallbackQuestion(
                        new Question(
                                "DTO와 Entity 분리",
                                "DTO와 Entity를 분리해서 사용하는 이유는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "DTO",
                        Set.of("DTO", "Entity", "Architecture")
                ),
                new FallbackQuestion(
                        new Question(
                                "GET Body 금지 이유",
                                "GET 요청에 Body를 사용하지 않는 이유는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "GET",
                        Set.of("HTTP", "GET", "REST")
                ),
                new FallbackQuestion(
                        new Question(
                                "쿠키 vs 세션",
                                "쿠키와 세션의 차이를 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "Session",
                        Set.of("Cookie", "Session", "Authentication")
                ),
                new FallbackQuestion(
                        new Question(
                                "JPA Entity",
                                "JPA에서 @Entity는 어떤 역할을 하나요?",
                                "BASIC",
                                null
                        ),
                        "JPA",
                        Set.of("JPA", "Entity", "ORM")
                ),
                new FallbackQuestion(
                        new Question(
                                "트랜잭션 기초",
                                "트랜잭션(Transaction)이란 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "ACID",
                        Set.of("Transaction", "ACID", "DB")
                ),
                new FallbackQuestion(
                        new Question(
                                "JSON vs XML",
                                "JSON과 XML의 차이점은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "JSON",
                        Set.of("JSON", "XML", "Serialization")
                ),
                new FallbackQuestion(
                        new Question(
                                "예외 처리 공통화",
                                "서버에서 예외 처리를 공통화하는 방법에는 어떤 것들이 있나요?",
                                "BASIC",
                                null
                        ),
                        "Exception",
                        Set.of("Exception", "Spring", "API")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        new Question(
                                "ACID 특성",
                                "트랜잭션의 ACID 특성을 각각 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "ACID",
                        Set.of("Transaction", "ACID", "DB")
                ),
                new FallbackQuestion(
                        new Question(
                                "JPA N+1",
                                "JPA에서 N+1 문제가 발생하는 원인과 해결 방법을 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "N+1",
                        Set.of("JPA", "N+1", "FetchJoin")
                ),
                new FallbackQuestion(
                        new Question(
                                "영속성 컨텍스트",
                                "영속성 컨텍스트의 역할과 장점은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "PersistenceContext",
                        Set.of("JPA", "PersistenceContext", "ORM")
                ),
                new FallbackQuestion(
                        new Question(
                                "인덱스 원리",
                                "인덱스(Index)의 동작 원리와 성능에 미치는 영향을 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "Index",
                        Set.of("DB", "Index", "Query")
                ),
                new FallbackQuestion(
                        new Question(
                                "JWT 인증 흐름",
                                "JWT 기반 인증의 동작 흐름을 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "JWT",
                        Set.of("JWT", "Authentication", "Security")
                ),
                new FallbackQuestion(
                        new Question(
                                "Access/Refresh 분리",
                                "Access Token과 Refresh Token을 분리해서 사용하는 이유는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "RefreshToken",
                        Set.of("JWT", "RefreshToken", "Security")
                ),
                new FallbackQuestion(
                        new Question(
                                "동시성 문제",
                                "동시성 문제란 무엇이며, 어떤 상황에서 발생하나요?",
                                "BASIC",
                                null
                        ),
                        "RaceCondition",
                        Set.of("Concurrency", "RaceCondition", "DB")
                ),
                new FallbackQuestion(
                        new Question(
                                "멱등성",
                                "REST API에서 멱등성(Idempotency)이 중요한 이유는 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Idempotency",
                        Set.of("REST", "Idempotency", "HTTP")
                ),
                new FallbackQuestion(
                        new Question(
                                "@Transactional 범위",
                                "@Transactional이 적용되는 범위와 주의점은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Transaction",
                        Set.of("Spring", "Transaction", "Proxy")
                ),
                new FallbackQuestion(
                        new Question(
                                "서버 페이징",
                                "페이징 처리를 서버에서 구현할 때 고려해야 할 사항은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "Paging",
                        Set.of("Paging", "DB", "Performance")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        new Question(
                                "격리 수준",
                                "트랜잭션 격리 수준(Read Uncommitted ~ Serializable)과 각 수준에서 발생 가능한 문제를 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "IsolationLevel",
                        Set.of("Transaction", "IsolationLevel", "DB")
                ),
                new FallbackQuestion(
                        new Question(
                                "DB 부하 절감",
                                "대용량 트래픽 환경에서 DB 부하를 줄이기 위한 전략에는 어떤 것들이 있나요?",
                                "BASIC",
                                null
                        ),
                        "Caching",
                        Set.of("Performance", "DB", "Caching")
                ),
                new FallbackQuestion(
                        new Question(
                                "지연 로딩 동작",
                                "JPA에서 지연 로딩(Lazy Loading)의 내부 동작 방식과 주의점을 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "LazyLoading",
                        Set.of("JPA", "LazyLoading", "Proxy")
                ),
                new FallbackQuestion(
                        new Question(
                                "분산 세션 관리",
                                "분산 환경에서 세션 관리를 어떻게 설계할 수 있나요?",
                                "BASIC",
                                null
                        ),
                        "DistributedSystem",
                        Set.of("Session", "DistributedSystem", "Redis")
                ),
                new FallbackQuestion(
                        new Question(
                                "서킷 브레이커",
                                "서킷 브레이커(Circuit Breaker)가 필요한 상황과 적용 시 주의할 점은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "CircuitBreaker",
                        Set.of("Resilience4j", "CircuitBreaker", "FaultTolerance")
                ),
                new FallbackQuestion(
                        new Question(
                                "캐시 정합성",
                                "데이터 정합성을 유지하면서 성능을 개선하기 위한 캐시 전략을 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "Consistency",
                        Set.of("Cache", "Consistency", "TTL")
                ),
                new FallbackQuestion(
                        new Question(
                                "멀티 인스턴스 동시성",
                                "멀티 인스턴스 환경에서 동시성 제어를 어떻게 처리할 수 있나요?",
                                "BASIC",
                                null
                        ),
                        "DistributedLock",
                        Set.of("Concurrency", "DistributedLock", "DB")
                ),
                new FallbackQuestion(
                        new Question(
                                "비동기 처리",
                                "비동기 처리(@Async, 메시지 큐 등)를 도입할 때의 장단점을 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "Async",
                        Set.of("Async", "MessageQueue", "Kafka")
                ),
                new FallbackQuestion(
                        new Question(
                                "RDB + NoSQL 기준",
                                "RDB와 NoSQL을 함께 사용하는 아키텍처에서의 설계 기준은 무엇인가요?",
                                "BASIC",
                                null
                        ),
                        "NoSQL",
                        Set.of("RDB", "NoSQL", "Architecture")
                ),
                new FallbackQuestion(
                        new Question(
                                "관측 가능성",
                                "장애 상황에서 로그, 메트릭, 트레이싱을 활용해 문제를 분석하는 절차를 설명해 주세요.",
                                "BASIC",
                                null
                        ),
                        "Observability",
                        Set.of("Observability", "Logging", "Tracing")
                )
        ));

        return Map.copyOf(map);
    }
}