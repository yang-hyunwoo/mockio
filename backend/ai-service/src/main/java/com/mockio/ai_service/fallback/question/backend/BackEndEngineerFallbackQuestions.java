package com.mockio.ai_service.fallback.question.backend;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BackEndEngineerFallbackQuestions {

    private BackEndEngineerFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<String>> byDifficulty() {
        Map<InterviewDifficulty, List<String>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                "REST API란 무엇이며, RESTful 하다는 것은 어떤 의미인가요?",
                "HTTP 메서드(GET, POST, PUT, DELETE)의 차이를 설명해 주세요.",
                "상태 코드 200, 400, 401, 403, 500의 의미를 각각 설명해 주세요.",
                "DTO와 Entity를 분리해서 사용하는 이유는 무엇인가요?",
                "GET 요청에 Body를 사용하지 않는 이유는 무엇인가요?",
                "쿠키와 세션의 차이를 설명해 주세요.",
                "JPA에서 @Entity는 어떤 역할을 하나요?",
                "트랜잭션(Transaction)이란 무엇인가요?",
                "JSON과 XML의 차이점은 무엇인가요?",
                "서버에서 예외 처리를 공통화하는 방법에는 어떤 것들이 있나요?"
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                "트랜잭션의 ACID 특성을 각각 설명해 주세요.",
                "JPA에서 N+1 문제가 발생하는 원인과 해결 방법을 설명해 주세요.",
                "영속성 컨텍스트의 역할과 장점은 무엇인가요?",
                "인덱스(Index)의 동작 원리와 성능에 미치는 영향을 설명해 주세요.",
                "JWT 기반 인증의 동작 흐름을 설명해 주세요.",
                "Access Token과 Refresh Token을 분리해서 사용하는 이유는 무엇인가요?",
                "동시성 문제란 무엇이며, 어떤 상황에서 발생하나요?",
                "REST API에서 멱등성(Idempotency)이 중요한 이유는 무엇인가요?",
                "@Transactional이 적용되는 범위와 주의점은 무엇인가요?",
                "페이징 처리를 서버에서 구현할 때 고려해야 할 사항은 무엇인가요?"
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                "트랜잭션 격리 수준(Read Uncommitted ~ Serializable)과 각 수준에서 발생 가능한 문제를 설명해 주세요.",
                "대용량 트래픽 환경에서 DB 부하를 줄이기 위한 전략에는 어떤 것들이 있나요?",
                "JPA에서 지연 로딩(Lazy Loading)의 내부 동작 방식과 주의점을 설명해 주세요.",
                "분산 환경에서 세션 관리를 어떻게 설계할 수 있나요?",
                "서킷 브레이커(Circuit Breaker)가 필요한 상황과 적용 시 주의할 점은 무엇인가요?",
                "데이터 정합성을 유지하면서 성능을 개선하기 위한 캐시 전략을 설명해 주세요.",
                "멀티 인스턴스 환경에서 동시성 제어를 어떻게 처리할 수 있나요?",
                "비동기 처리(@Async, 메시지 큐 등)를 도입할 때의 장단점을 설명해 주세요.",
                "RDB와 NoSQL을 함께 사용하는 아키텍처에서의 설계 기준은 무엇인가요?",
                "장애 상황에서 로그, 메트릭, 트레이싱을 활용해 문제를 분석하는 절차를 설명해 주세요."
        ));

        return Map.copyOf(map);
    }
}
