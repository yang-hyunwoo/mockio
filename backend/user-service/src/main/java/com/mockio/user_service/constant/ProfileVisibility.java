package com.mockio.user_service.constant;

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