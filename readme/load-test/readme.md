## 🚥 부하 테스트
[🔝 메인 목차로 이동](../../readme.md)

서비스의 병목 구간과 응답 시간을 확인하기 위해 `k6`를 사용해 부하 테스트를 진행했습니다.  
수집된 메트릭은 `InfluxDB`에 적재하고, `Grafana` 대시보드를 통해 응답 시간, 처리량, 가상 사용자 수를 시각적으로 분석했습니다.

### 테스트 환경
- 테스트 도구: `k6`
- 메트릭 저장소: `InfluxDB`
- 시각화 도구: `Grafana`

### 테스트 대상
- 로그인
- 회원가입
- 면접 생성

### 회원 가입
<details>
<summary>회원 가입 부하 테스트</summary>

<p align="center">
  <img src="./그라파나_회원가입_분석.png" width="48%" />
  <img src="./회원가입_100명_30초.png" width="48%" />
</p>

부하 테스트 스크립트는 [`k6/signup-test.js`](../../k6/signup-test.js) 에서 확인할 수 있습니다.

📊 테스트 결과 해석

```
- avg=1.99s
- med=1.92s
- p95=2.9s
- 실패율 0%
```

✔ 긍정적인 점<br/>
모든 요청이 정상 처리되어 **실패율이 0%**로 나타남<br/>
동시 사용자 100명 수준에서도 시스템이 안정적으로 동작<br/>
p95가 3초 이내로, 부하 상황에서도 완전히 붕괴하지 않음

⚠ 아쉬운 점<br/>
중앙값(median)이 1.92초로, 대부분의 요청이 약 2초에 가까운 응답 시간을 보임<br/>
일부 요청만 느린 것이 아니라 전체적으로 응답 시간이 높은 상태<br/>
회원가입 API 기준으로는 사용자가 체감하기에 다소 느린 응답 속도<br/>

📌 분석

해당 결과는 특정 구간의 병목이 아닌,<br/>
요청 처리 과정 전반에서 일정한 비용이 발생하고 있음을 의미합니다.

특히 다음 요소들이 주요 원인으로 판단됩니다:

bcrypt 해싱 과정에서 발생하는 CPU 연산 비용<br/>
HTTPS 및 Gateway를 거치는 네트워크 처리 비용<br/>
회원가입 시 발생하는 다수의 DB write 및 트랜잭션 commit 비용<br/>

✅ 결론

시스템은 동시 요청 상황에서도 안정적으로 동작함을 확인<br/>
다만 평균 및 중앙값이 약 2초 수준으로 나타나<br/>
응답 성능 개선 여지가 존재

향후에는 다음과 같은 방향으로 개선을 고려할 수 있습니다:

bcrypt cost factor 조정 또는 인증 서버 분리<br/>
DB write 구조 최적화
</details>


### 로그인
<details>
<summary>로그인 부하 테스트</summary>

<p align="center">
  <img src="./동기_로그인.png" width="48%" />
  <img src="./비동기_로그인.png" width="48%" />
</p>

부하 테스트 스크립트는 [`k6/login-test.js`](../../k6/signup-test.js) 에서 확인할 수 있습니다.

📊 테스트 결과 해석

```
동기                  비동기
- avg=2.67s         avg=907ms
- med=2.35s         med=959ms
- p95=6.9s          1.92s
- 실패율 0%          실패율 0%
```

### 🔧 로그인 처리 구조 개선 (동기 → 비동기 전환)

로그인 성능 저하의 원인을 분석한 결과,<br/>
인증 성공 이후 user-service로 전달되는 동기 HTTP 호출이 추가 latency를 유발하고 있음을 확인했습니다.<br/>
기존 구조에서는 로그인 요청 시 외부 서비스 응답을 기다리는 blocking 방식으로 처리되어, <br/>
동시 요청이 증가할 경우 응답 지연과 tail latency(p95)가 크게 증가하는 문제가 발생했습니다.<br/>
이를 해결하기 위해 해당 로직을 Kafka 기반 비동기 이벤트 처리 방식으로 전환했습니다.

#### Before (동기 구조)

로그인 → 인증 → user-service HTTP 호출 → 응답 반환

- 외부 서비스 응답을 기다리는 blocking 구조
- user-service 장애가 로그인 실패로 전파
- 동시 요청 증가 시 스레드 점유로 인해 지연 발생

#### After (비동기 구조)

로그인 → 인증 → Kafka 이벤트 발행 → 응답 반환

- 외부 서비스 호출 제거
- 로그인 핵심 경로와 부가 로직 분리
- kafka 이벤트 기반으로 user-service에서 후처리 수행

## 📊 개선 효과
- median: 약 2.5s → 1.46s
- p95: 약 8s → 2.48s

동기 HTTP 호출 제거를 통해 tail latency를 크게 개선했으며, 로그인 요청 처리의 안정성을 확보했습니다.

### ⚠ 아쉬운 점 및 개선 방향

현재 구조에서는 Kafka 이벤트를 직접 발행하는 방식으로 구현되어 있어, 이벤트 유실 가능성이 존재합니다.<br/>
로그인 성공 이벤트는 치명적인 데이터는 아니지만,<br/>
보다 높은 신뢰성이 요구되는 경우 Outbox 패턴을 적용하여 메시지 전달 보장을 강화할 수 있습니다.<br/>
또한 로그인 응답 시간은 여전히 bcrypt 비밀번호 검증 비용에 의해 영향을 받고 있어,<br/>
향후 인증 서버 스케일 아웃 또는 cost factor 조정 등의 추가적인 성능 개선이 필요합니다.

<details>
<summary>소스 변경 점</summary>

```
동기 방식 개선 
    public LoginResponse login(UserLoginRequest request , HttpServletResponse response) {
            ...
        userClient.resetFailCount(loginUser.getUserId());
    }

    public void resetFailCount(Long userId) {
            if (userId == null) {
                throw new CustomApiException(ERR_001.getHttpStatus(), ERR_001, "유저 정보는 " + ERR_001);
            }
            userRestClient.patch()
                    .uri("/api/users/v1/internal/login-success")
                    .body(new LoginSuccessRequest(userId))
                    .retrieve()
                    .onStatus(status -> status.value() == 404, (req, res) -> {
                        throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
                    })
                    .onStatus(status -> status.is5xxServerError(), (req, res) -> {
                        throw new CustomApiException(ILLEGALSTATE.getHttpStatus(), ILLEGALSTATE,"user-service reset fail 5xx 호출");
                    })
                    .onStatus(status -> status.is4xxClientError(), (req, res) -> {
                        throw new CustomApiException(ERR_000.getHttpStatus(), ERR_000,"user-service reset fail 4xx 호출 실패");
                    })
                    .toBodilessEntity();
    }
    
    비동기 kafka 이벤트
    public LoginResponse login(UserLoginRequest request , HttpServletResponse response) {
                 ...
         loginSuccessEventProducer.publish(loginUser.getUserId());
     }
     
     public class LoginSuccessEventProducer {

            private static final String TOPIC = "user.login.success";
        
            private final KafkaTemplate<String, String> kafkaTemplate;
            private final ObjectMapper objectMapper;
        
            public void publish(Long userId) {
                try {
                    LoginSuccessEvent event = new LoginSuccessEvent(userId, OffsetDateTime.now());
                    String payload = objectMapper.writeValueAsString(event);
        
                    kafkaTemplate.send(TOPIC, String.valueOf(userId), payload);
                    log.info("published login success event. userId={}", userId);
                } catch (JsonProcessingException e) {
                    log.error("failed to serialize login success event. userId={}", userId, e);
                } catch (Exception e) {
                    log.error("failed to publish login success event. userId={}", userId, e);
                }
            }
    }
    
    core-user-service
     @KafkaListener(topics = "user.login.success", groupId = "core-service-login-success")
     public void consume(String message) {
        try {
            LoginSuccessEvent event = objectMapper.readValue(message, LoginSuccessEvent.class);
            userService.resetFailCount(event.userId());
            log.info("consumed login success event. userId={}", event.userId());
        } catch (Exception e) {
            log.error("failed to consume login success event. message={}", message, e);
            throw new RuntimeException(e);
        }
    }
     
```

 </details>

</details>
