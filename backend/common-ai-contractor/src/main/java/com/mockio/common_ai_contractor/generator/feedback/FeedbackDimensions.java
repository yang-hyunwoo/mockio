package com.mockio.common_ai_contractor.generator.feedback;

import io.swagger.v3.oas.annotations.media.Schema;

public record FeedbackDimensions(

        @Schema(description = "구조성", example = "0")
        Integer structure,

        @Schema(description = "명확성", example = "0")
        Integer clarity,

        @Schema(description = "구체성", example = "0")
        Integer specificity

) {
    public FeedbackDimensions {
        if (structure == null) structure = 0;
        if (clarity == null) clarity = 0;
        if (specificity == null) specificity = 0;
    }
}
