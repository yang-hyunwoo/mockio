package com.mockio.ai_service.fallback.question.server;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ServerEngineerFallbackQuestions {

    private ServerEngineerFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<String>> byDifficulty() {
        Map<InterviewDifficulty, List<String>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                "서버와 클라이언트의 차이는 무엇인가요?",
                "리눅스 서버를 사용하는 이유는 무엇인가요?",
                "포트란 무엇인가요?",
                "로그 파일은 왜 중요한가요?",
                "방화벽의 역할은 무엇인가요?",
                "서버 재시작이 필요한 상황은 언제인가요?",
                "HTTPS는 왜 필요한가요?",
                "환경 변수는 왜 사용하나요?",
                "CPU와 메모리의 차이는 무엇인가요?",
                "서버 모니터링이 필요한 이유는 무엇인가요?"
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                "Load Balancer의 역할은 무엇인가요?",
                "무중단 배포 전략을 설명해 주세요.",
                "서버 장애 원인을 파악하는 절차는 무엇인가요?",
                "로그 로테이션이 필요한 이유는 무엇인가요?",
                "컨테이너와 VM의 차이를 설명해 주세요.",
                "Blue-Green 배포란 무엇인가요?",
                "디스크 I/O 병목은 어떻게 확인하나요?",
                "세션 클러스터링은 왜 필요한가요?",
                "서버 보안 패치가 중요한 이유는 무엇인가요?",
                "DNS의 동작 원리를 설명해 주세요."
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                "대규모 트래픽에서 서버 스케일링 전략은 무엇인가요?",
                "SPOF를 제거하는 방법은 무엇인가요?",
                "장애 대응 Runbook이 필요한 이유는 무엇인가요?",
                "서버 간 시간 동기화가 중요한 이유는 무엇인가요?",
                "TCP 튜닝이 필요한 상황은 언제인가요?",
                "장애 복구 시 데이터 무결성을 어떻게 보장하나요?",
                "멀티 리전 구성의 장단점은 무엇인가요?",
                "비용 최적화를 위한 서버 설계 방법은 무엇인가요?",
                "로그 기반 알림의 한계는 무엇인가요?",
                "인프라 관점에서 서킷 브레이커를 설명해 주세요."
        ));

        return Map.copyOf(map);
    }

}
