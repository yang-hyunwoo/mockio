package com.mockio.auth_service.kafka.processor;


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

    public void callExternal(String payloadJson) {
        KeycloakDisableUserPayload payload = parse(payloadJson);
        keycloakUserClient.disableUser(payload.keycloakUserId());
    }

    private KeycloakDisableUserPayload parse(String payloadJson) {
        try {
            return objectMapper.readValue(payloadJson, KeycloakDisableUserPayload.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid payload json", e);
        }
    }

}
