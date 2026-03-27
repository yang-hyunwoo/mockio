## 📂 프로젝트 구조
[🔝 메인 목차로 이동](../../readme.md)

mockio는 MSA 구조로 구성되어 있으며, 서비스별 역할 분리와 공통 모듈화를 통해 유지보수성과 확장성을 고려했습니다.
<br/>

```
mockio
├── backend
│ ├── auth-service # 🔐 인증 서비스 (JWT, JWKS)
│ ├── core-service # 🎯 핵심 비즈니스 (인터뷰, 피드백)
│ ├── gateway # 🌐 API Gateway (라우팅)
│ ├── support-service # 🛠 보조 서비스(faq, 알림, 공지)
│ │
│ ├── common-core # 공통 도메인 로직
│ ├── common-jpa # JPA 설정 및 공통 엔티티
│ ├── common-security # 인증/인가 공통 모듈
│ ├── common-kafka # Kafka 공통 설정
│ ├── common-spring # Spring 공통 설정
│ └── common-ai-contractor # AI 연동 인터페이스
│
└── docker # ⚙️ 배포 및 인프라 설정

```