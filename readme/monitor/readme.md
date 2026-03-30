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
