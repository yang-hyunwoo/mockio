package com.mockio.core_service.interview.dto.response;

public record FeedbackDimensions(
        Integer structure, //구조성
        Integer clarity,    //명확성
        Integer specificity //구체성
) {
    public FeedbackDimensions {
        if (structure == null) structure = 0;
        if (clarity == null) clarity = 0;
        if (specificity == null) specificity = 0;
    }
}
