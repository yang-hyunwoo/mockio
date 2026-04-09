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
                                "REST API란 무엇이며, RESTful 하다는 것은 어떤 의미인가요?"
                        ),
                        new Question(
                                "REST API 설계 원칙",
                                "RESTful API를 설계할 때 URI, HTTP 메서드, 상태 코드를 어떻게 일관되게 구성해야 하나요?"
                        ),
                        "REST",
                        Set.of("REST", "HTTP", "API")
                ),
                new FallbackQuestion(
                        new Question(
                                "HTTP 메서드",
                                "HTTP 메서드(GET, POST, PUT, DELETE)의 차이를 설명해 주세요."
                        ),
                        new Question(
                                "HTTP 메서드 선택 기준",
                                "조회, 생성, 수정, 삭제 상황에서 각 HTTP 메서드를 어떤 기준으로 선택해야 하나요?"
                        ),
                        "Method",
                        Set.of("HTTP", "REST", "Method")
                ),
                new FallbackQuestion(
                        new Question(
                                "HTTP 상태 코드",
                                "상태 코드 200, 400, 401, 403, 500의 의미를 각각 설명해 주세요."
                        ),
                        new Question(
                                "상태 코드 구분 기준",
                                "인증 실패와 권한 실패를 401, 403으로 구분할 때 어떤 기준으로 판단해야 하나요?"
                        ),
                        "StatusCode",
                        Set.of("HTTP", "StatusCode", "REST")
                ),
                new FallbackQuestion(
                        new Question(
                                "DTO와 Entity 분리",
                                "DTO와 Entity를 분리해서 사용하는 이유는 무엇인가요?"
                        ),
                        new Question(
                                "DTO 분리의 실무 이점",
                                "DTO와 Entity를 분리하지 않았을 때 발생할 수 있는 문제를 실무 관점에서 설명해 주세요."
                        ),
                        "DTO",
                        Set.of("DTO", "Entity", "Architecture")
                ),
                new FallbackQuestion(
                        new Question(
                                "GET Body 금지 이유",
                                "GET 요청에 Body를 사용하지 않는 이유는 무엇인가요?"
                        ),
                        new Question(
                                "GET 요청과 캐싱",
                                "GET 요청에서 Body를 사용하지 않는 관례가 캐싱, 프록시, 브라우저 동작과 어떤 관련이 있나요?"
                        ),
                        "GET",
                        Set.of("HTTP", "GET", "REST")
                ),
                new FallbackQuestion(
                        new Question(
                                "쿠키 vs 세션",
                                "쿠키와 세션의 차이를 설명해 주세요."
                        ),
                        new Question(
                                "인증 방식 선택 기준",
                                "로그인 상태 유지 기능을 구현할 때 쿠키와 세션을 어떻게 조합해 설계하시겠습니까?"
                        ),
                        "Session",
                        Set.of("Cookie", "Session", "Authentication")
                ),
                new FallbackQuestion(
                        new Question(
                                "JPA Entity",
                                "JPA에서 @Entity는 어떤 역할을 하나요?"
                        ),
                        new Question(
                                "Entity 설계 주의점",
                                "JPA Entity를 설계할 때 기본 생성자, 식별자, equals/hashCode에서 주의할 점은 무엇인가요?"
                        ),
                        "JPA",
                        Set.of("JPA", "Entity", "ORM")
                ),
                new FallbackQuestion(
                        new Question(
                                "트랜잭션 기초",
                                "트랜잭션(Transaction)이란 무엇인가요?"
                        ),
                        new Question(
                                "트랜잭션 필요 사례",
                                "계좌 이체나 주문 처리 같은 기능에서 트랜잭션이 왜 반드시 필요한지 예시로 설명해 주세요."
                        ),
                        "ACID",
                        Set.of("Transaction", "ACID", "DB")
                ),
                new FallbackQuestion(
                        new Question(
                                "JSON vs XML",
                                "JSON과 XML의 차이점은 무엇인가요?"
                        ),
                        new Question(
                                "포맷 선택 기준",
                                "API 응답 포맷으로 JSON이 XML보다 더 자주 사용되는 이유를 설명해 주세요."
                        ),
                        "JSON",
                        Set.of("JSON", "XML", "Serialization")
                ),
                new FallbackQuestion(
                        new Question(
                                "예외 처리 공통화",
                                "서버에서 예외 처리를 공통화하는 방법에는 어떤 것들이 있나요?"
                        ),
                        new Question(
                                "예외 응답 설계",
                                "Spring 기반 API 서버에서 예외 응답 포맷을 일관되게 설계하려면 어떤 구조가 필요할까요?"
                        ),
                        "Exception",
                        Set.of("Exception", "Spring", "API")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        new Question(
                                "ACID 특성",
                                "트랜잭션의 ACID 특성을 각각 설명해 주세요."
                        ),
                        new Question(
                                "ACID와 실무 장애",
                                "실무에서 ACID 중 하나라도 보장되지 않으면 어떤 문제가 발생할 수 있는지 예를 들어 설명해 주세요."
                        ),
                        "ACID",
                        Set.of("Transaction", "ACID", "DB")
                ),
                new FallbackQuestion(
                        new Question(
                                "JPA N+1",
                                "JPA에서 N+1 문제가 발생하는 원인과 해결 방법을 설명해 주세요."
                        ),
                        new Question(
                                "N+1 해결 전략 비교",
                                "fetch join, EntityGraph, batch size 방식은 각각 어떤 상황에서 적합한가요?"
                        ),
                        "N+1",
                        Set.of("JPA", "N+1", "FetchJoin")
                ),
                new FallbackQuestion(
                        new Question(
                                "영속성 컨텍스트",
                                "영속성 컨텍스트의 역할과 장점은 무엇인가요?"
                        ),
                        new Question(
                                "1차 캐시와 변경 감지",
                                "영속성 컨텍스트의 1차 캐시와 변경 감지가 성능과 개발 생산성에 어떤 영향을 주는지 설명해 주세요."
                        ),
                        "PersistenceContext",
                        Set.of("JPA", "PersistenceContext", "ORM")
                ),
                new FallbackQuestion(
                        new Question(
                                "인덱스 원리",
                                "인덱스(Index)의 동작 원리와 성능에 미치는 영향을 설명해 주세요."
                        ),
                        new Question(
                                "인덱스 부작용",
                                "인덱스를 많이 생성했을 때 조회 성능 외에 쓰기 성능과 저장 공간 측면에서 어떤 trade-off가 있나요?"
                        ),
                        "Index",
                        Set.of("DB", "Index", "Query")
                ),
                new FallbackQuestion(
                        new Question(
                                "JWT 인증 흐름",
                                "JWT 기반 인증의 동작 흐름을 설명해 주세요."
                        ),
                        new Question(
                                "JWT 보안 고려사항",
                                "JWT 인증을 실제 서비스에 적용할 때 만료, 재발급, 탈취 대응은 어떻게 설계하시겠습니까?"
                        ),
                        "JWT",
                        Set.of("JWT", "Authentication", "Security")
                ),
                new FallbackQuestion(
                        new Question(
                                "Access/Refresh 분리",
                                "Access Token과 Refresh Token을 분리해서 사용하는 이유는 무엇인가요?"
                        ),
                        new Question(
                                "토큰 재발급 시나리오",
                                "Refresh Token이 탈취되었을 가능성을 고려할 때 재발급 정책은 어떻게 설계하는 것이 좋을까요?"
                        ),
                        "RefreshToken",
                        Set.of("JWT", "RefreshToken", "Security")
                ),
                new FallbackQuestion(
                        new Question(
                                "동시성 문제",
                                "동시성 문제란 무엇이며, 어떤 상황에서 발생하나요?"
                        ),
                        new Question(
                                "동시성 제어 방법",
                                "재고 차감이나 좋아요 증가처럼 동시에 같은 데이터를 수정할 때 어떤 방식으로 동시성 문제를 해결할 수 있나요?"
                        ),
                        "RaceCondition",
                        Set.of("Concurrency", "RaceCondition", "DB")
                ),
                new FallbackQuestion(
                        new Question(
                                "멱등성",
                                "REST API에서 멱등성(Idempotency)이 중요한 이유는 무엇인가요?"
                        ),
                        new Question(
                                "멱등성 보장 설계",
                                "결제 요청이나 중복 제출 방지가 필요한 API에서 멱등성을 어떻게 보장할 수 있나요?"
                        ),
                        "Idempotency",
                        Set.of("REST", "Idempotency", "HTTP")
                ),
                new FallbackQuestion(
                        new Question(
                                "@Transactional 범위",
                                "@Transactional이 적용되는 범위와 주의점은 무엇인가요?"
                        ),
                        new Question(
                                "프록시 기반 한계",
                                "Spring의 프록시 기반 @Transactional이 self-invocation 상황에서 왜 동작하지 않는지 설명해 주세요."
                        ),
                        "Transaction",
                        Set.of("Spring", "Transaction", "Proxy")
                ),
                new FallbackQuestion(
                        new Question(
                                "서버 페이징",
                                "페이징 처리를 서버에서 구현할 때 고려해야 할 사항은 무엇인가요?"
                        ),
                        new Question(
                                "Offset vs Cursor",
                                "대용량 데이터 조회에서 offset 기반 페이징과 cursor 기반 페이징의 차이와 선택 기준을 설명해 주세요."
                        ),
                        "Paging",
                        Set.of("Paging", "DB", "Performance")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        new Question(
                                "격리 수준",
                                "트랜잭션 격리 수준(Read Uncommitted ~ Serializable)과 각 수준에서 발생 가능한 문제를 설명해 주세요."
                        ),
                        new Question(
                                "격리 수준 선택 기준",
                                "실제 서비스에서 격리 수준을 너무 높이거나 낮게 설정했을 때 어떤 문제가 생기며, 어떤 기준으로 선택해야 하나요?"
                        ),
                        "IsolationLevel",
                        Set.of("Transaction", "IsolationLevel", "DB")
                ),
                new FallbackQuestion(
                        new Question(
                                "DB 부하 절감",
                                "대용량 트래픽 환경에서 DB 부하를 줄이기 위한 전략에는 어떤 것들이 있나요?"
                        ),
                        new Question(
                                "부하 절감 우선순위",
                                "캐시, 읽기 복제본, 샤딩, 비동기화 중 어떤 전략부터 검토할지 판단 기준을 설명해 주세요."
                        ),
                        "Caching",
                        Set.of("Performance", "DB", "Caching")
                ),
                new FallbackQuestion(
                        new Question(
                                "지연 로딩 동작",
                                "JPA에서 지연 로딩(Lazy Loading)의 내부 동작 방식과 주의점을 설명해 주세요."
                        ),
                        new Question(
                                "프록시와 예외 상황",
                                "Lazy Loading이 프록시 기반으로 동작할 때 발생할 수 있는 예외와 성능 문제를 설명해 주세요."
                        ),
                        "LazyLoading",
                        Set.of("JPA", "LazyLoading", "Proxy")
                ),
                new FallbackQuestion(
                        new Question(
                                "분산 세션 관리",
                                "분산 환경에서 세션 관리를 어떻게 설계할 수 있나요?"
                        ),
                        new Question(
                                "세션 저장소 선택",
                                "멀티 서버 환경에서 세션 스토리지를 Redis로 둘 때의 장점과 주의점을 설명해 주세요."
                        ),
                        "DistributedSystem",
                        Set.of("Session", "DistributedSystem", "Redis")
                ),
                new FallbackQuestion(
                        new Question(
                                "서킷 브레이커",
                                "서킷 브레이커(Circuit Breaker)가 필요한 상황과 적용 시 주의할 점은 무엇인가요?"
                        ),
                        new Question(
                                "장애 전파 차단 설계",
                                "외부 API 장애가 내부 서비스 전체로 전파되지 않도록 서킷 브레이커를 어떻게 설계하시겠습니까?"
                        ),
                        "CircuitBreaker",
                        Set.of("Resilience4j", "CircuitBreaker", "FaultTolerance")
                ),
                new FallbackQuestion(
                        new Question(
                                "캐시 정합성",
                                "데이터 정합성을 유지하면서 성능을 개선하기 위한 캐시 전략을 설명해 주세요."
                        ),
                        new Question(
                                "캐시 갱신 전략 비교",
                                "cache-aside, write-through, write-behind 전략의 차이와 정합성 측면의 trade-off를 설명해 주세요."
                        ),
                        "Consistency",
                        Set.of("Cache", "Consistency", "TTL")
                ),
                new FallbackQuestion(
                        new Question(
                                "멀티 인스턴스 동시성",
                                "멀티 인스턴스 환경에서 동시성 제어를 어떻게 처리할 수 있나요?"
                        ),
                        new Question(
                                "분산 락 선택 기준",
                                "DB 락, Redis 분산 락, 메시지 큐 직렬화 방식 중 어떤 방식이 적절한지 판단 기준을 설명해 주세요."
                        ),
                        "DistributedLock",
                        Set.of("Concurrency", "DistributedLock", "DB")
                ),
                new FallbackQuestion(
                        new Question(
                                "비동기 처리",
                                "비동기 처리(@Async, 메시지 큐 등)를 도입할 때의 장단점을 설명해 주세요."
                        ),
                        new Question(
                                "비동기 아키텍처 주의점",
                                "메시지 큐 기반 비동기 처리 도입 시 순서 보장, 중복 소비, 재처리 전략은 어떻게 설계해야 하나요?"
                        ),
                        "Async",
                        Set.of("Async", "MessageQueue", "Kafka")
                ),
                new FallbackQuestion(
                        new Question(
                                "RDB + NoSQL 기준",
                                "RDB와 NoSQL을 함께 사용하는 아키텍처에서의 설계 기준은 무엇인가요?"
                        ),
                        new Question(
                                "저장소 분리 판단",
                                "어떤 데이터를 RDB에 두고 어떤 데이터를 NoSQL에 둘지 조회 패턴과 정합성 요구사항 기준으로 설명해 주세요."
                        ),
                        "NoSQL",
                        Set.of("RDB", "NoSQL", "Architecture")
                ),
                new FallbackQuestion(
                        new Question(
                                "관측 가능성",
                                "장애 상황에서 로그, 메트릭, 트레이싱을 활용해 문제를 분석하는 절차를 설명해 주세요."
                        ),
                        new Question(
                                "관측 데이터 상관 분석",
                                "로그, 메트릭, 분산 트레이싱을 함께 사용할 때 장애 원인을 좁혀가는 실전 접근 방식을 설명해 주세요."
                        ),
                        "Observability",
                        Set.of("Observability", "Logging", "Tracing")
                )
        ));

        return Map.copyOf(map);
    }
}