package com.mockio.auth_service.kafka.dto;

/**
 * 사용자 라이프사이클 변경을 표현하는 도메인 이벤트 DTO.
 *
 * <p>사용자 생성, 수정, 삭제 등 주요 상태 변화가 발생했을 때
 * Kafka를 통해 전파되는 이벤트 계약 객체이다.</p>
 *
 * <p>이 이벤트는 소비자 서비스(Auth Service 등)에서
 * 후속 작업(계정 비활성화, 권한 정리 등)을 트리거하는 용도로 사용된다.</p>
 */

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "사용자 라이프사이클 도메인 이벤트")
public record UserLifecycleEvent(

        @Schema(description = "이벤트 고유 식별자", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID eventId,

        @Schema(description = "이벤트가 속한 집계 타입", example = "USER")
        String aggregateType,

        @Schema(description = "집계 식별자", example = "12345")
        Long aggregateId,

        @Schema(description = "이벤트 타입", example = "USER_DELETED")
        String eventType,

        @Schema(description = "이벤트 페이로드(JSON)")
        JsonNode payload,

        @Schema(description = "이벤트 발생 시각(UTC 기준)", example = "2024-03-01T12:34:56Z")
        OffsetDateTime occurredAt
) {}

