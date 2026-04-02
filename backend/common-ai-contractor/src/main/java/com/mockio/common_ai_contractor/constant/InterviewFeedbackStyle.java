package com.mockio.common_ai_contractor.constant;

/**
 * 면접 피드백 enum
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InterviewFeedbackStyle {
    STRICT("직설적으로"),
    COACHING("균형 있게"),
    FRIENDLY("부드럽게")
    ;

    private final String label;

}
