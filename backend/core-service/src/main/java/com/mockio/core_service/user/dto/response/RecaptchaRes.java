package com.mockio.core_service.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


public record RecaptchaRes(
        @Schema(description = "리캡차 성공 여부" , example = "true")
        boolean success,

        @Schema(description = "리캡차 점수" , example = "50")
        float score,

         String action,

         String hostname
) {}