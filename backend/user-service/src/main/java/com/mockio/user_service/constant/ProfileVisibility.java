package com.mockio.user_service.constant;

/** 유저 공개 여부  enum
 *  ProfileVisibility
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProfileVisibility {
    PUBLIC("공개"),
    PRIVATE("비공개")
    ;

    private final String label;

}