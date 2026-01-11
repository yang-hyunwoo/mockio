package com.mockio.auth_service.kafka.dto;

/**
 * Keycloak 사용자 비활성화 요청에 사용되는 이벤트 페이로드 DTO.
 *
 * <p>사용자 라이프사이클 이벤트에 따라
 * Keycloak 계정을 비활성화하기 위한 최소 정보를 담는다.</p>
 *
 * <p>주로 Outbox 패턴 및 Kafka 메시지 페이로드로 사용된다.</p>
 */

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Keycloak 사용자 비활성화 이벤트 페이로드")
public record KeycloakDisableUserPayload(
        @Schema(description = "이벤트 고유 식별자", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID eventId,

        @Schema(description = "비활성화 대상 Keycloak 사용자 ID")
        String keycloakUserId,

        @Schema(description = "비활성화 사유", example = "USER_DELETED")
        String reason
) {}
