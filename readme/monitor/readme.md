## 📊 Monitoring 
[🔝 메인 목차로 이동](../../readme.md)

서비스 안정성을 확보하기 위해 Prometheus + Grafana 기반의 모니터링 시스템을 구축했습니다.

- 애플리케이션 메트릭 수집
- 실시간 상태 시각화
- 장애 발생 시 알림 전송 (Slack)

### 🏗 Architecture

- Spring Boot (Actuator + Micrometer)
  → /actuator/prometheus 노출

- Prometheus
  → 서비스 메트릭 수집 및 저장

- Grafana
  → 대시보드 시각화

- Slack Webhook
  → 장애 발생 시 알림 전송

### 📈 Metrics

다음과 같은 주요 메트릭을 수집합니다.

- HTTP 요청 수 및 응답 시간
    - `http_server_requests_seconds`

- JVM 상태
    - `jvm_memory_used_bytes`
    - `jvm_gc_pause_seconds`

- DB 커넥션 풀 상태
    - `hikaricp_connections_active`
    - `hikaricp_connections_pending`

- 서비스 상태
    - `up`


### 🚨 Alerting

이상 상황 발생 시 Slack으로 알림을 전송합니다.

- 서버 다운 감지
    - `up == 0`

- 500 에러 급증
    - `rate(http_server_requests_seconds_count{status="500"}[5m])`


### 🛠 Troubleshooting

모니터링 구축 과정에서 다음과 같은 문제를 해결했습니다.

- Docker 네트워크 분리로 인한 Prometheus scrape 실패
- 컨테이너 간 통신 시 내부 포트/외부 포트 혼동 문제
- 특정 서비스에서 `/actuator/prometheus` 미노출로 인한 메트릭 수집 실패
- Grafana 데이터가 컨테이너 재시작 시 초기화되는 문제
  → Volume 설정으로 해결


### ✅ Result

- 장애 발생 시 즉각적인 감지 및 대응 가능
- 서비스 상태를 실시간으로 파악 가능
- DB 커넥션 및 트래픽 병목 지점 식별 가능