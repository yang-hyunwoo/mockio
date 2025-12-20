package com.mockio.user_service.constant;

/** 면접 난이도 enum
 *  InterviewDifficulty
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InterviewDifficulty {
    EASY("쉬움"),
    MEDIUM("보통"),
    HARD("어려움"),
    ;

    private final String label;
}
