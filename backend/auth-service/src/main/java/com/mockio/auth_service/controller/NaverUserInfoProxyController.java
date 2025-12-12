package com.mockio.auth_service.controller;

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
public class NaverUserInfoProxyController {

    private final WebClient webClient;

    public NaverUserInfoProxyController(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://openapi.naver.com")
                .build();
    }

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
     * 네이버 응답:
     * {
     *   "resultcode": "00",
     *   "message": "success",
     *   "response": { ... 실제 데이터 ... }
     * }
     *
     * → 아래처럼 평탄화해서 Keycloak 에 전달:
     * {
     *   "id": "...",
     *   "email": "...",
     *   "name": "...",
     *   "nickname": "..."
     * }
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
