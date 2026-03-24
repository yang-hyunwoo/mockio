package com.mockio.core_service.feedback.dto.response;

public record InternalFeedbackDimensions(
        Integer structure, //구조성
        Integer clarity,    //명확성
        Integer specificity //구체성
) {
    public InternalFeedbackDimensions {
        if (structure == null) structure = 0;
        if (clarity == null) clarity = 0;
        if (specificity == null) specificity = 0;
    }
}
