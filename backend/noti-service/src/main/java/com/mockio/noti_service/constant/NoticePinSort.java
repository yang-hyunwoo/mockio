package com.mockio.noti_service.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NoticePinSort {

    NORMAL("기본"),
    IMPORTANT("중요") ,
    ;

    private final String label;

}
