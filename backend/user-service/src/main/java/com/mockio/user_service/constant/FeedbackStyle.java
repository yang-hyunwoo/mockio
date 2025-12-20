package com.mockio.user_service.constant;

/** 면접 피드백 enum
 *  FeedbackStyle
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FeedbackStyle {
    STRICT("엄격"),
    COACHING("조언"),
    FRIENDLY("친근")
    ;

    private final String label;
}
