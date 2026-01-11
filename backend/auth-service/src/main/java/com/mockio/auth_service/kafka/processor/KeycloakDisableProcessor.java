package com.mockio.auth_service.kafka.processor;

/**
 * Keycloak 사용자 비활성화 작업을 수행하는 Outbox 실행 프로세서.
 *
 * <p>Outbox에 적재된 KeycloakDisableUserPayload를 역직렬화하여,
 * Keycloak Admin API를 통해 실제 사용자 비활성화 호출을 수행한다.</p>
 *
 * <p>이 클래스는 Kafka/Outbox 처리 파이프라인의
 * 외부 시스템 호출 경계 역할을 담당한다.</p>
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.client.KeycloakUserClient;
import com.mockio.auth_service.kafka.dto.KeycloakDisableUserPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakDisableProcessor {

    private final ObjectMapper objectMapper;

    private final KeycloakUserClient keycloakUserClient;

    /**
     * Outbox 이벤트 페이로드를 기반으로 Keycloak 사용자 비활성화를 수행한다.
     *
     * <p>페이로드(JSON)를 DTO로 변환한 뒤,
     * KeycloakUserClient를 통해 외부 Keycloak Admin API를 호출한다.</p>
     *
     * @param payloadJson Outbox에 저장된 페이로드(JSON 문자열)
     * @throws IllegalArgumentException 페이로드 형식이 올바르지 않은 경우
     */
    public void callExternal(String payloadJson) {
        KeycloakDisableUserPayload payload = parse(payloadJson);
        keycloakUserClient.disableUser(payload.keycloakUserId());
    }

    /**
     * JSON 문자열을 KeycloakDisableUserPayload로 역직렬화한다.
     *
     * @param payloadJson 페이로드 JSON 문자열
     * @return 파싱된 KeycloakDisableUserPayload
     * @throws IllegalArgumentException JSON 형식이 올바르지 않은 경우
     */
    private KeycloakDisableUserPayload parse(String payloadJson) {
        try {
            return objectMapper.readValue(payloadJson, KeycloakDisableUserPayload.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid payload json", e);
        }
    }

}
