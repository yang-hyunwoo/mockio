package com.mockio.core_service.feedback.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record InternalFeedbackDimensions(

        @Schema(description = "구조성", example = "0")
        Integer structure,

        @Schema(description = "명확성", example = "0")
        Integer clarity,

        @Schema(description = "구체성", example = "0")
        Integer specificity

) {
    public InternalFeedbackDimensions {
        if (structure == null) structure = 0;
        if (clarity == null) clarity = 0;
        if (specificity == null) specificity = 0;
    }
}
