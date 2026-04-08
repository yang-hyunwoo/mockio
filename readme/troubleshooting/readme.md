
## ⚠️ 트러블슈팅
[🔝 메인 목차로 이동](../../readme.md)

<details>
<summary> 🔐 Outbox & Inbox 패턴 구조 개선 (2026.01)</summary>

#### 📌 문제 상황
- 초기에는 Outbox 및 이벤트 처리 로직을 별도의 서비스로 분리하여 구성
- 서비스 간 통신(Kafka, API)을 통해 이벤트를 전달하는 구조

#### 🔍 원인
- 서비스 분리로 인해 다음과 같은 문제 발생:
    - 서비스 간 네트워크 호출 증가
    - 배포 및 운영 복잡도 증가
    - 트래픽이 적은 환경에서 과도한 MSA 구조
- Outbox 패턴의 특성상 DB 트랜잭션과 밀접하게 연결되어 있어 별도 서비스로 분리할 경우 오히려 구조가 복잡해짐

#### 🛠 해결
- Outbox 및 Inbox(멱등 처리) 로직을 core-service 내부로 통합
  - 서비스는 통합하되, Producer / Consumer / Domain 역할을 분리하여 구조적 경계를 유지
- Outbox
    - 이벤트를 동일 트랜잭션 내에서 DB에 저장
    - Worker를 통해 Kafka로 발행

- Inbox
    - ProcessedEvent 테이블 기반 중복 이벤트 처리 방지
    - eventId 기준 멱등성 보장

#### ✅ 결과
- 서비스 간 불필요한 통신 제거
- 구조 단순화 및 운영 효율성 향상
- 트랜잭션 기반 이벤트 처리 안정성 확보
- 프로젝트 규모에 맞는 현실적인 MSA 구조로 개선
</details>



<details>
<summary> 🔐 인증 구조 개선: Keycloak → JWT 전환 (2026.03)</summary>

#### 📌 문제 상황
- Keycloak 기반 인증을 사용했으나 설정 및 운영 복잡도가 높음
- Keycloak과 별도로 내부 JWT를 관리하면서 인증 흐름이 복잡해짐
- 커스터마이징이 제한적

#### 🔍 원인
- 외부 인증 서버에 의존하는 구조
- MSA 환경에서 인증 흐름 제어 어려움
- OAuth2 기반 인증은 적용했으나, Keycloak을 통한 외부 인증 서버 구조가 프로젝트 규모 대비 과도하여 운영 복잡도가 증가함
- 필요한 수준의 인증 기능은 JWT 기반으로 충분히 구현 가능하다고 판단

#### 🛠 해결
- JWT 기반 인증 구조로 변경
- RSA 기반 서명 적용
- JWKS 엔드포인트를 통해 공개키 제공 및 서비스 간 JWT 검증 구조 설계

#### ✅ 결과
- 인증 구조 단순화
- 서비스 간 인증 처리 유연성 확보
- 외부 의존성 제거
- 인증 서버 중심 구조에서 서비스 간 독립적인 검증 구조로 전환
- 배포 서버 메모리 감소

</details>



<details>
<summary> 📨 Kafka Consumer 동작 문제 (2026.03)</summary>

#### 📌 문제 상황
- `interview.lifecycle` 토픽으로 이벤트 발행은 정상적으로 수행되었음
- Producer 로그에서는 `published topic: interview.lifecycle` 가 확인되었음
- 하지만 `@KafkaListener` 내부의 `kafka message received` 로그가 출력되지 않아 Consumer가 메시지를 처리하지 못하는 문제가 발생함

#### 🔍 원인
- 초기에는 애플리케이션 코드 문제(`@KafkaListener`, groupId, AckMode, 역직렬화 설정 등)를 의심했음
- 그러나 Kafka 컨테이너 내부에서 `kafka-console-consumer`, `kafka-consumer-groups` 명령어도 모두 `TimeoutException` 으로 실패함
- 원인을 분석한 결과, **단일 브로커(KRaft single-node) 환경에서 Consumer Group Coordinator 및 내부 토픽(`__consumer_offsets`) 관련 설정이 부족**하여 Consumer Group 동작이 정상적으로 이루어지지 않는 상태였음

#### 🛠 해결
- Kafka가 단일 브로커 환경으로 동작하고 있음을 확인
- 내부 토픽과 Consumer Group 처리를 위해 아래 설정을 추가함

```yaml
KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
KAFKA_DEFAULT_REPLICATION_FACTOR: 1
KAFKA_NUM_PARTITIONS: 1
KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"
```

#### ✅ 결과
kafka-consumer-groups, kafka-console-consumer 명령이 정상 동작함
@KafkaListener 에서 이벤트 수신이 정상적으로 이루어짐
Producer는 동작하지만 Consumer만 실패하던 문제를 해결하면서, 싱글 브로커 Kafka 환경에서 필요한 내부 설정의 중요성을 확인할 수 있었음
</details>


<details>
<summary> 📊 AI 면접 평가 점수 일관성 문제 (2026.04)</summary>

#### 📌 문제 상황
- AI 면접 평가 결과가 질문마다 일관되지 않고,
  동일한 수준의 답변임에도 점수가 다르게 산정되는 문제가 발생했습니다.
- 특히 GPT가 생성하는 평가가 기준 없이 이루어지면서,
  사용자 입장에서 결과를 신뢰하기 어려운 상황이었습니다.

#### 🔍 원인
- 기존 평가 구조는 명확한 평가 기준 없이 AI가 답변을 종합적으로 판단하는 방식이었습니다.
- 이로 인해 질문 유형이나 표현 방식에 따라 평가 기준이 달라지고,
  기술적 정확성, 실무 적합성, 구체성 등의 요소가 일관되게 반영되지 않았습니다.
- 또한 총평 단계에서도 동일 기준 없이 재평가가 이루어지면서,
  질문별 점수와 전체 점수 간의 불일치가 발생할 수 있었습니다.

#### 🛠 해결
- 직무별 평가 기준(rubric)을 정의하고, 질문별 평가에 적용하도록 구조를 개선했습니다.
- rubric은 문제 이해, 설계 능력, 성능 고려, 트레이드오프 등 실무 중심 항목으로 구성했습니다.
- AI가 rubric을 참고하여 점수를 산정하도록 프롬프트를 개선하고,
  점수 일관성을 유지하도록 평가 기준을 명확히 했습니다.
- 총평은 rubric을 재사용하지 않고,
  질문별 평가 결과를 기반으로 종합 요약하도록 구조를 분리했습니다.

#### 📊 개선 결과
- 동일 수준의 답변에 대해 일관된 점수 산정이 가능해졌습니다.
- 질문 유형이 달라도 동일한 기준으로 평가되어 결과의 신뢰도가 향상되었습니다.
- 질문별 평가와 총평 간의 점수 불일치 문제가 개선되었으며,
  전체 면접 평가의 안정성과 일관성이 확보되었습니다.

</details>

