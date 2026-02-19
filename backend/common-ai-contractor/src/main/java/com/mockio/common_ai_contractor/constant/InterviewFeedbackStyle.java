package com.mockio.common_ai_contractor.constant;

/** 면접 피드백 enum
 *  FeedbackStyle
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InterviewFeedbackStyle {
    STRICT("엄격"),
    COACHING("조언"),
    FRIENDLY("친근")
    ;

    private final String label;

}
