package com.mockio.auth_service.kafka.dto;

/**
 * Auth Service Outbox 이벤트 처리 작업(Task)을 표현하는 DTO.
 *
 * <p>Outbox 테이블에서 조회된 이벤트를 워커로 전달하기 위한
 * 실행 단위 객체이며, 재시도 정책 판단에 필요한 정보를 포함한다.</p>
 */

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Auth Service Outbox 이벤트 처리 작업")
public record OutboxAuthEventTask(

        @Schema(description = "Outbox 이벤트 ID")
        Long id,

        @Schema(description = "집계(aggregate) 식별자")
        String aggregateId,

        @Schema(description = "외부 시스템으로 전달될 이벤트 페이로드(JSON)")
        JsonNode payload,

        @Schema(description = "현재 시도 횟수", example = "1")
        int attemptCount,

        @Schema(description = "최대 시도 횟수", example = "3")
        int maxAttempts
) {}
