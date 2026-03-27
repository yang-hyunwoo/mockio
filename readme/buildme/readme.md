## 🚀 실행 방법

[🔝 메인 목차로 이동](../../readme.md)

## 🧪 Local 실행

### 1. 저장소 클론

```bash
git clone https://github.com/yang-hyunwoo/mockio.git
```

### 2. 환경 변수 설정
- .env 파일 생성
- application.yml 파일 생성

### 3.Docker 실행
docker compose -f docker-compose.local.yml up --build   
- redis
- ollma
- kafka
- postgre

---

### 🚀 Production
- Docker 기반 배포
- IWINV 서버 운영
- GitHub Actions를 통한 이미지 빌드 자동화 (CI)
- 서버에서 Docker pull 기반 배포
