## ⚙️ 개발 환경

[🔝 메인 목차로 이동](../../readme.md)

## 🏗 아키텍처

mockio는 이벤트 기반 아키텍처(Event-Driven Architecture)를 기반으로 구성되어 있으며,  
서비스 간 결합도를 낮추고 비동기 처리를 통해 확장성과 안정성을 개선했습니다.
사용자의 인터뷰 진행 과정에서 발생하는 이벤트를 Kafka를 통해 비동기 처리하며,  
Outbox 패턴을 적용하여 데이터 정합성과 안정성을 확보했습니다.

### 🔄 처리 흐름

1. 사용자가 인터뷰를 진행하며 이벤트 발생
2. 이벤트를 Outbox 테이블에 저장
3. Kafka로 이벤트 발행
4. Consumer에서 이벤트 수신
5. AI 피드백 생성 및 DB 저장
---
## 🧰 기술 스택

### 🧩 Backend (Microservices)
- Java, Spring Boot, JPA (Hibernate)
- Spring Security, JWT

### 🎨 Frontend
- Next.js, TypeScript

### 📨 Messaging
- Apache Kafka
    - 이벤트 기반 비동기 처리
    - Outbox 패턴 적용

### 🗄 Database
- PostgreSQL
- Flyway

### ⚙️ Infra
- Docker
- IWINV (Linux 기반 클라우드 서버)

### 🔐 Authentication
JWT를 비대칭키(RSA) 기반으로 서명하고, JWKS 엔드포인트를 통해 공개키를 제공하여  
다른 서비스에서도 토큰을 검증할 수 있도록 구성했습니다.

- JWT 기반 인증 적용
- JWKS 엔드포인트 제공
- 서비스 간 토큰 검증 지원

### 🧩 Additional Tools
- Redis (캐싱 및 토큰 관리)
- Cloudinary (이미지 업로드 및 관리)
- reCAPTCHA (봇 방지)
- open AI (AI)


## 📊 Monitoring 
- prometheus + grafana + slack

