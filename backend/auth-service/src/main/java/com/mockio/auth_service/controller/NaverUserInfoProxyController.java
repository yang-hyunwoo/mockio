package com.mockio.auth_service.controller;

/**
 * 네이버 OAuth UserInfo API를 프록시하는 컨트롤러.
 *
 * <p>Keycloak Social Login 연동 과정에서,
 * Keycloak이 전달한 Access Token을 그대로 사용하여
 * 네이버 UserInfo API(/v1/nid/me)를 호출한다.</p>
 *
 * <p>네이버 원본 응답을 Keycloak이 기대하는 평탄화(flat) 구조로 변환하여 반환한다.</p>
 */

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/v1")
public class NaverUserInfoProxyController {

    private final WebClient webClient;

    public NaverUserInfoProxyController(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://openapi.naver.com")
                .build();
    }

    /**
     * 네이버 사용자 정보를 조회하여 Keycloak에 전달한다.
     *
     * <p>요청 헤더의 Authorization(Bearer Token)을 그대로
     * 네이버 UserInfo API 호출에 전달하는 프록시 엔드포인트이다.</p>
     *
     * @param authorization Authorization 헤더 (Bearer Access Token)
     * @return 평탄화된 네이버 사용자 정보
     */
    @GetMapping("/naver/userinfo")
    public Mono<Map<String, Object>> userInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        // Keycloak → 서버로 들어온 Authorization: Bearer {accessToken}
        // → 이걸 그대로 네이버 userinfo 에 전달
        return webClient.get()
                .uri("/v1/nid/me")
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::flattenResponse);
    }


    /**
     * 네이버 UserInfo API 응답을 평탄화한다.
     *
     * <p>네이버 응답의 response 필드 내부 값만 추출하여,
     * Keycloak에서 사용 가능한 단순 Key-Value 형태로 변환한다.</p>
     *
     * @param original 네이버 UserInfo API 원본 응답
     * @return 평탄화된 사용자 정보 맵
     *     네이버 응답:
     *     {
     *     "resultcode": "00",
     *        "message": "success",
     *        "response": { ... 실제 데이터 ... }
     *      }
     *      → 아래처럼 평탄화해서 Keycloak 에 전달:
     *      {
     *      "id": "...",
     *        "email": "...",
     *        "name": "...",
     *        "nickname": "..."
     *      }
     *
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> flattenResponse(Map<String, Object> original) {
        Object responseObj = original.get("response");

        Map<String, Object> response = responseObj instanceof Map
                ? (Map<String, Object>) responseObj
                : Map.of();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id",        response.get("id"));
        result.put("email",     response.get("email"));
        result.put("name",      response.get("name"));
        result.put("nickname",  response.get("nickname"));

        return result;
    }

}
