package com.mockio.common_ai_contractor.constant;

/** 면접 모드 enum
 *  InterviewMode
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InterviewMode {
    TEXT("텍스트"),
    VOICE("목소리"),
    ;

    private final String label;
}
