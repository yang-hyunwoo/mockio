package com.mockio.core_service.interview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record FeedbackJobMetric(

        @Schema(description = "실무 적합성", example = "0")
        Integer practicality,

        @Schema(description = "의사결정 기준", example = "0")
        Integer decisionMaking,

        @Schema(description = "트레이드오프 이해", example = "0")
        Integer tradeoff

) {
    public FeedbackJobMetric {
        if (practicality == null) practicality = 0;
        if (decisionMaking == null) decisionMaking = 0;
        if (tradeoff == null) tradeoff = 0;
    }

}
