package com.mockio.ai_service.fallback.question.server;

import com.mockio.ai_service.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServerEngineerFallbackQuestions {

    private ServerEngineerFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<FallbackQuestion>> byDifficulty() {
        Map<InterviewDifficulty, List<FallbackQuestion>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                new FallbackQuestion(
                        "서버 vs 클라이언트",
                        "서버와 클라이언트의 차이는 무엇인가요?",
                        Set.of("ClientServer", "Architecture", "Network")
                ),
                new FallbackQuestion(
                        "리눅스 서버 사용 이유",
                        "리눅스 서버를 사용하는 이유는 무엇인가요?",
                        Set.of("Linux", "Server", "Infrastructure")
                ),
                new FallbackQuestion(
                        "포트 개념",
                        "포트란 무엇인가요?",
                        Set.of("Port", "TCP", "Network")
                ),
                new FallbackQuestion(
                        "로그 파일 중요성",
                        "로그 파일은 왜 중요한가요?",
                        Set.of("Logging", "Monitoring", "Troubleshooting")
                ),
                new FallbackQuestion(
                        "방화벽 역할",
                        "방화벽의 역할은 무엇인가요?",
                        Set.of("Firewall", "Security", "Network")
                ),
                new FallbackQuestion(
                        "서버 재시작 상황",
                        "서버 재시작이 필요한 상황은 언제인가요?",
                        Set.of("Restart", "Deployment", "Troubleshooting")
                ),
                new FallbackQuestion(
                        "HTTPS 필요성",
                        "HTTPS는 왜 필요한가요?",
                        Set.of("HTTPS", "TLS", "Security")
                ),
                new FallbackQuestion(
                        "환경 변수 사용 이유",
                        "환경 변수는 왜 사용하나요?",
                        Set.of("EnvironmentVariable", "Configuration", "Security")
                ),
                new FallbackQuestion(
                        "CPU vs 메모리",
                        "CPU와 메모리의 차이는 무엇인가요?",
                        Set.of("CPU", "Memory", "Performance")
                ),
                new FallbackQuestion(
                        "서버 모니터링",
                        "서버 모니터링이 필요한 이유는 무엇인가요?",
                        Set.of("Monitoring", "Alerting", "Availability")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        "Load Balancer 역할",
                        "Load Balancer의 역할은 무엇인가요?",
                        Set.of("LoadBalancer", "Scaling", "HighAvailability")
                ),
                new FallbackQuestion(
                        "무중단 배포 전략",
                        "무중단 배포 전략을 설명해 주세요.",
                        Set.of("ZeroDowntime", "Deployment", "DevOps")
                ),
                new FallbackQuestion(
                        "장애 원인 분석 절차",
                        "서버 장애 원인을 파악하는 절차는 무엇인가요?",
                        Set.of("IncidentResponse", "Troubleshooting", "Monitoring")
                ),
                new FallbackQuestion(
                        "로그 로테이션",
                        "로그 로테이션이 필요한 이유는 무엇인가요?",
                        Set.of("LogRotation", "DiskManagement", "Ops")
                ),
                new FallbackQuestion(
                        "컨테이너 vs VM",
                        "컨테이너와 VM의 차이를 설명해 주세요.",
                        Set.of("Container", "VM", "Virtualization")
                ),
                new FallbackQuestion(
                        "Blue-Green 배포",
                        "Blue-Green 배포란 무엇인가요?",
                        Set.of("BlueGreen", "Deployment", "DevOps")
                ),
                new FallbackQuestion(
                        "디스크 I/O 병목",
                        "디스크 I/O 병목은 어떻게 확인하나요?",
                        Set.of("IO", "Performance", "Monitoring")
                ),
                new FallbackQuestion(
                        "세션 클러스터링",
                        "세션 클러스터링은 왜 필요한가요?",
                        Set.of("SessionClustering", "HighAvailability", "Scaling")
                ),
                new FallbackQuestion(
                        "보안 패치 중요성",
                        "서버 보안 패치가 중요한 이유는 무엇인가요?",
                        Set.of("SecurityPatch", "Vulnerability", "Compliance")
                ),
                new FallbackQuestion(
                        "DNS 동작 원리",
                        "DNS의 동작 원리를 설명해 주세요.",
                        Set.of("DNS", "Network", "Resolution")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        "대규모 스케일링",
                        "대규모 트래픽에서 서버 스케일링 전략은 무엇인가요?",
                        Set.of("AutoScaling", "LoadBalancing", "Scalability")
                ),
                new FallbackQuestion(
                        "SPOF 제거",
                        "SPOF를 제거하는 방법은 무엇인가요?",
                        Set.of("SPOF", "HighAvailability", "Redundancy")
                ),
                new FallbackQuestion(
                        "Runbook 필요성",
                        "장애 대응 Runbook이 필요한 이유는 무엇인가요?",
                        Set.of("Runbook", "IncidentManagement", "Operations")
                ),
                new FallbackQuestion(
                        "시간 동기화",
                        "서버 간 시간 동기화가 중요한 이유는 무엇인가요?",
                        Set.of("NTP", "TimeSync", "DistributedSystem")
                ),
                new FallbackQuestion(
                        "TCP 튜닝",
                        "TCP 튜닝이 필요한 상황은 언제인가요?",
                        Set.of("TCP", "Networking", "Performance")
                ),
                new FallbackQuestion(
                        "데이터 무결성 보장",
                        "장애 복구 시 데이터 무결성을 어떻게 보장하나요?",
                        Set.of("DataIntegrity", "Backup", "Recovery")
                ),
                new FallbackQuestion(
                        "멀티 리전 구성",
                        "멀티 리전 구성의 장단점은 무엇인가요?",
                        Set.of("MultiRegion", "Latency", "Availability")
                ),
                new FallbackQuestion(
                        "비용 최적화 설계",
                        "비용 최적화를 위한 서버 설계 방법은 무엇인가요?",
                        Set.of("CostOptimization", "Cloud", "Infrastructure")
                ),
                new FallbackQuestion(
                        "로그 기반 알림 한계",
                        "로그 기반 알림의 한계는 무엇인가요?",
                        Set.of("Logging", "Alerting", "Observability")
                ),
                new FallbackQuestion(
                        "서킷 브레이커 인프라 관점",
                        "인프라 관점에서 서킷 브레이커를 설명해 주세요.",
                        Set.of("CircuitBreaker", "Resilience", "FaultTolerance")
                )
        ));

        return Map.copyOf(map);
    }

}
