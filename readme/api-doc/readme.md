## 📘 API Documentation

[🔝 메인 목차로 이동](../../../readme.md)

📘 API Documentation

본 프로젝트의 API 명세는 Scalar 기반으로 제공되며,
조회 전용(Read-only) 문서로 구성되어 있습니다.

아래 링크를 통해 각 서비스별 API 스펙을 확인할 수 있습니다.
- 👉 https://mockio.cloud/api-docs


📡 API Reference

- 🔐 Auth Service
    - 인증 / 인가 관련 API

- 📡 Core Service
    - 핵심 비즈니스 로직 API
- 🧩 Support Service
  - 부가 기능 및 지원 API


👀 How to Use
- 위 링크 접속
- 좌측에서 API 선택
- 요청(Request) / 응답(Response) 구조 확인

🔒 Access Policy
- 본 API 문서는 테스트 기능이 비활성화된 상태입니다.
- (Try it out, Request 실행 기능 제거)
- 실제 API 호출은 별도의 클라이언트 또는 서버 환경에서 진행해야 합니다.


🔐 Authentication
 - 인증이 필요한 API는 아래 헤더를 기준으로 설계되어 있습니다.
 - ``` Authorization: Bearer {ACCESS_TOKEN} ```

※ 해당 문서에서는 토큰 입력 및 테스트 기능은 제공되지 않습니다.


🚨 Error Response

- 공통 에러 응답 형식:
    ```
    {
    "timestamp": "2026-04-04T12:00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Invalid input",
    "path": "/api/example"
    }
    ```


📌 Notes
- 본 문서는 API 구조 이해를 위한 명세서입니다.
- 각 서비스는 MSA 구조로 독립적으로 구성되어 있습니다.
- API는 지속적으로 업데이트될 수 있습니다.