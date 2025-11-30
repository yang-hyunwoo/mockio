docker run -d --name keycloak -p 8084:8080   -e KEYCLOAK_ADMIN={name}   -e KEYCLOAK_ADMIN_PASSWORD=${password}   -v ${themepath}/mockio-theme:/opt/keycloak/themes/mockio-theme   quay.io/keycloak/keycloak:24.0  start-dev

임시 토큰 확인 여부
curl -X POST "http://localhost:8084/realms/study-realm/protocol/openid-connect/token"   -H "Content-Type: application/x-www-form-urlencoded"   -d "grant_type=password&client_id=gateway&username=testuser&password=test1234"

1.keycloak 접속 localhost:8084 <br/>
2.왼쪽 상단 드랍박스 클릭 -> create realm으로 컨테이너 생성<br/>
3.clients -> create client -> gateway 후 생성 [return url 등등은 추후 설정 예정] <br/>
4.Users -> create new user 로 테스트 유저 생성 <br/>
5.realm settings->themes에서 login theme를 내가 설정한 거로 변경 가능 [디자인은 추후 다듬을 예정]

```
임시
docker rm -f keycloak
docker run -d --name keycloak -p 8084:8080   -e KEYCLOAK_ADMIN={name}   -e KEYCLOAK_ADMIN_PASSWORD=${password}   -v ${themepath}/mockio-theme:/opt/keycloak/themes/mockio-theme   quay.io/keycloak/keycloak:24.0  start-dev
curl -X POST "http://localhost:8084/realms/study-realm/protocol/openid-connect/token"   -H "Content-Type: application/x-www-form-urlencoded"   -d "grant_type=password&client_id=gateway&username=testuser&password=test1234"

```