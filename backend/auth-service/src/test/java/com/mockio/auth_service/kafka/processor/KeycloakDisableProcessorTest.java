package com.mockio.auth_service.kafka.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.client.KeycloakUserClient;
import com.mockio.auth_service.kafka.dto.KeycloakDisableUserPayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakDisableProcessorTest {

    @Mock ObjectMapper objectMapper;
    @Mock KeycloakUserClient keycloakUserClient;

    @InjectMocks KeycloakDisableProcessor processor;

    @Test
    void callExternal_success_parsesPayload_and_callsKeycloak() throws Exception {
        // given
        String json = "{\"any\":true}";
        KeycloakDisableUserPayload payload =
                new KeycloakDisableUserPayload(UUID.randomUUID(), "kc-123", "USER_DELETED");

        given(objectMapper.readValue(json, KeycloakDisableUserPayload.class)).willReturn(payload);

        // when
        processor.callExternal(json);

        // then
        then(keycloakUserClient).should().disableUser("kc-123");
    }

    @Test
    void callExternal_whenPayloadInvalid_throwsIllegalArgumentException() throws Exception {
        // given
        String badJson = "{invalid-json";
        given(objectMapper.readValue(badJson, KeycloakDisableUserPayload.class))
                .willThrow(new RuntimeException("parse error"));

        // when & then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> processor.callExternal(badJson));

        assertTrue(ex.getMessage().contains("Invalid payload json"));
        then(keycloakUserClient).shouldHaveNoInteractions();
    }
}