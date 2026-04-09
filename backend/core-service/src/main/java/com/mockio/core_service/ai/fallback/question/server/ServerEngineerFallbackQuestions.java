package com.mockio.core_service.ai.fallback.question.server;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.core_service.ai.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.generator.question.Question;

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
                        new Question("서버 vs 클라이언트", "서버와 클라이언트의 차이는 무엇인가요?"),
                        new Question("클라이언트-서버 구조", "클라이언트-서버 구조에서 역할 분리가 중요한 이유는 무엇인가요?"),
                        "ClientServer",
                        Set.of("ClientServer", "Architecture", "Network")
                ),
                new FallbackQuestion(
                        new Question("리눅스 서버 사용 이유", "리눅스 서버를 사용하는 이유는 무엇인가요?"),
                        new Question("리눅스 선택 기준", "리눅스가 서버 환경에서 Windows보다 선호되는 이유는 무엇인가요?"),
                        "Linux",
                        Set.of("Linux", "Server", "Infrastructure")
                ),
                new FallbackQuestion(
                        new Question("포트 개념", "포트란 무엇인가요?"),
                        new Question("포트 활용", "하나의 서버에서 여러 서비스를 포트를 통해 어떻게 구분하나요?"),
                        "Port",
                        Set.of("Port", "TCP", "Network")
                ),
                new FallbackQuestion(
                        new Question("로그 파일 중요성", "로그 파일은 왜 중요한가요?"),
                        new Question("로그 활용", "로그를 활용해 장애를 분석하는 기본 절차는 무엇인가요?"),
                        "Logging",
                        Set.of("Logging", "Monitoring", "Troubleshooting")
                ),
                new FallbackQuestion(
                        new Question("방화벽 역할", "방화벽의 역할은 무엇인가요?"),
                        new Question("보안 정책", "방화벽 규칙을 설계할 때 고려해야 할 보안 원칙은 무엇인가요?"),
                        "Firewall",
                        Set.of("Firewall", "Security", "Network")
                ),
                new FallbackQuestion(
                        new Question("서버 재시작 상황", "서버 재시작이 필요한 상황은 언제인가요?"),
                        new Question("재시작 리스크", "서버 재시작 시 서비스 영향도를 줄이기 위한 방법은 무엇인가요?"),
                        "Restart",
                        Set.of("Restart", "Deployment", "Troubleshooting")
                ),
                new FallbackQuestion(
                        new Question("HTTPS 필요성", "HTTPS는 왜 필요한가요?"),
                        new Question("TLS 동작", "HTTPS에서 TLS가 데이터를 보호하는 방식은 무엇인가요?"),
                        "HTTPS",
                        Set.of("HTTPS", "TLS", "Security")
                ),
                new FallbackQuestion(
                        new Question("환경 변수 사용 이유", "환경 변수는 왜 사용하나요?"),
                        new Question("설정 관리 전략", "환경 변수를 활용해 설정을 분리하는 것이 중요한 이유는 무엇인가요?"),
                        "EnvironmentVariable",
                        Set.of("EnvironmentVariable", "Configuration", "Security")
                ),
                new FallbackQuestion(
                        new Question("CPU vs 메모리", "CPU와 메모리의 차이는 무엇인가요?"),
                        new Question("성능 병목 분석", "CPU와 메모리 중 어떤 자원이 병목인지 판단하는 방법은 무엇인가요?"),
                        "CPU",
                        Set.of("CPU", "Memory", "Performance")
                ),
                new FallbackQuestion(
                        new Question("서버 모니터링", "서버 모니터링이 필요한 이유는 무엇인가요?"),
                        new Question("모니터링 지표", "서버 상태를 파악하기 위한 핵심 모니터링 지표는 무엇인가요?"),
                        "Monitoring",
                        Set.of("Monitoring", "Alerting", "Availability")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        new Question("Load Balancer 역할", "Load Balancer의 역할은 무엇인가요?"),
                        new Question("로드밸런싱 전략", "Round Robin, Least Connection 방식의 차이는 무엇인가요?"),
                        "LoadBalancer",
                        Set.of("LoadBalancer", "Scaling", "HighAvailability")
                ),
                new FallbackQuestion(
                        new Question("무중단 배포 전략", "무중단 배포 전략을 설명해 주세요."),
                        new Question("배포 전략 비교", "Rolling, Blue-Green, Canary 배포의 차이는 무엇인가요?"),
                        "ZeroDowntime",
                        Set.of("ZeroDowntime", "Deployment", "DevOps")
                ),
                new FallbackQuestion(
                        new Question("장애 원인 분석 절차", "서버 장애 원인을 파악하는 절차는 무엇인가요?"),
                        new Question("Incident 대응", "장애 대응 시 우선순위를 어떻게 결정하나요?"),
                        "IncidentResponse",
                        Set.of("IncidentResponse", "Troubleshooting", "Monitoring")
                ),
                new FallbackQuestion(
                        new Question("로그 로테이션", "로그 로테이션이 필요한 이유는 무엇인가요?"),
                        new Question("로그 관리 전략", "로그 파일이 디스크를 가득 채우지 않도록 관리하는 방법은 무엇인가요?"),
                        "LogRotation",
                        Set.of("LogRotation", "DiskManagement", "Ops")
                ),
                new FallbackQuestion(
                        new Question("컨테이너 vs VM", "컨테이너와 VM의 차이를 설명해 주세요."),
                        new Question("선택 기준", "컨테이너와 VM을 어떤 기준으로 선택해야 하나요?"),
                        "Container",
                        Set.of("Container", "VM", "Virtualization")
                ),
                new FallbackQuestion(
                        new Question("Blue-Green 배포", "Blue-Green 배포란 무엇인가요?"),
                        new Question("배포 리스크 관리", "Blue-Green 배포에서 트래픽 전환 시 주의할 점은 무엇인가요?"),
                        "BlueGreen",
                        Set.of("BlueGreen", "Deployment", "DevOps")
                ),
                new FallbackQuestion(
                        new Question("디스크 I/O 병목", "디스크 I/O 병목은 어떻게 확인하나요?"),
                        new Question("I/O 최적화", "I/O 병목을 줄이기 위한 방법은 무엇인가요?"),
                        "IO",
                        Set.of("IO", "Performance", "Monitoring")
                ),
                new FallbackQuestion(
                        new Question("세션 클러스터링", "세션 클러스터링은 왜 필요한가요?"),
                        new Question("세션 관리 전략", "분산 환경에서 세션을 관리하는 방법은 무엇인가요?"),
                        "SessionClustering",
                        Set.of("SessionClustering", "HighAvailability", "Scaling")
                ),
                new FallbackQuestion(
                        new Question("보안 패치 중요성", "서버 보안 패치가 중요한 이유는 무엇인가요?"),
                        new Question("패치 전략", "운영 중단 없이 보안 패치를 적용하는 방법은 무엇인가요?"),
                        "SecurityPatch",
                        Set.of("SecurityPatch", "Vulnerability", "Compliance")
                ),
                new FallbackQuestion(
                        new Question("DNS 동작 원리", "DNS의 동작 원리를 설명해 주세요."),
                        new Question("DNS 성능 문제", "DNS 조회 지연이 발생할 때 원인을 어떻게 분석하나요?"),
                        "DNS",
                        Set.of("DNS", "Network", "Resolution")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        new Question("대규모 스케일링", "대규모 트래픽에서 서버 스케일링 전략은 무엇인가요?"),
                        new Question("스케일링 전략 비교", "수직 확장과 수평 확장의 차이는 무엇인가요?"),
                        "AutoScaling",
                        Set.of("AutoScaling", "LoadBalancing", "Scalability")
                ),
                new FallbackQuestion(
                        new Question("SPOF 제거", "SPOF를 제거하는 방법은 무엇인가요?"),
                        new Question("고가용성 설계", "고가용성을 위해 어떤 아키텍처를 구성해야 하나요?"),
                        "SPOF",
                        Set.of("SPOF", "HighAvailability", "Redundancy")
                ),
                new FallbackQuestion(
                        new Question("Runbook 필요성", "장애 대응 Runbook이 필요한 이유는 무엇인가요?"),
                        new Question("Runbook 구성", "효과적인 Runbook은 어떤 내용을 포함해야 하나요?"),
                        "Runbook",
                        Set.of("Runbook", "IncidentManagement", "Operations")
                ),
                new FallbackQuestion(
                        new Question("시간 동기화", "서버 간 시간 동기화가 중요한 이유는 무엇인가요?"),
                        new Question("시간 불일치 문제", "시간이 맞지 않을 경우 어떤 문제가 발생하나요?"),
                        "NTP",
                        Set.of("NTP", "TimeSync", "DistributedSystem")
                ),
                new FallbackQuestion(
                        new Question("TCP 튜닝", "TCP 튜닝이 필요한 상황은 언제인가요?"),
                        new Question("네트워크 최적화", "TCP 성능을 개선하기 위한 주요 튜닝 포인트는 무엇인가요?"),
                        "TCP",
                        Set.of("TCP", "Networking", "Performance")
                ),
                new FallbackQuestion(
                        new Question("데이터 무결성 보장", "장애 복구 시 데이터 무결성을 어떻게 보장하나요?"),
                        new Question("복구 전략", "데이터 손실을 최소화하기 위한 백업 전략은 무엇인가요?"),
                        "DataIntegrity",
                        Set.of("DataIntegrity", "Backup", "Recovery")
                ),
                new FallbackQuestion(
                        new Question("멀티 리전 구성", "멀티 리전 구성의 장단점은 무엇인가요?"),
                        new Question("멀티 리전 설계", "멀티 리전에서 데이터 일관성을 어떻게 유지하나요?"),
                        "MultiRegion",
                        Set.of("MultiRegion", "Latency", "Availability")
                ),
                new FallbackQuestion(
                        new Question("비용 최적화 설계", "비용 최적화를 위한 서버 설계 방법은 무엇인가요?"),
                        new Question("비용-성능 균형", "성능과 비용 사이에서 최적의 균형을 어떻게 찾나요?"),
                        "CostOptimization",
                        Set.of("CostOptimization", "Cloud", "Infrastructure")
                ),
                new FallbackQuestion(
                        new Question("로그 기반 알림 한계", "로그 기반 알림의 한계는 무엇인가요?"),
                        new Question("Observability 전략", "로그, 메트릭, 트레이싱을 함께 사용하는 이유는 무엇인가요?"),
                        "Observability",
                        Set.of("Logging", "Alerting", "Observability")
                ),
                new FallbackQuestion(
                        new Question("서킷 브레이커 인프라 관점", "인프라 관점에서 서킷 브레이커를 설명해 주세요."),
                        new Question("장애 전파 방지", "서킷 브레이커가 장애 전파를 막는 방식은 무엇인가요?"),
                        "CircuitBreaker",
                        Set.of("CircuitBreaker", "Resilience", "FaultTolerance")
                )
        ));

        return Map.copyOf(map);
    }
}