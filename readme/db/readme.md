## 📦 Entity , Dto

### 핵심 도메인 ERD
<img src="/readme/db/mermaid-diagram%20(1).png">

### 이벤트 처리 ERD
<img src="/readme/db/mermaid-diagram%20(2).png">

```
mockio는 인터뷰 도메인과 이벤트 처리 도메인을 분리하여 설계했습니다.
핵심 비즈니스 데이터는 `users`, `interviews`, `interview_questions`, `interview_answers`,
`interview_feedbacks`, `interview_summary_feedbacks` 중심으로 관리하고,
이벤트 정합성과 멱등 처리를 위해 `outbox_interview_events`, `processed_events` 테이블을 별도로 두었습니다.
```
<br/>
<br/>

#### flyway 파일 경로
```
support-service/src/main/resources/db/migration
auth-service/src/main/resources/db/migration
core-service/src/main/resources/db/migration
```