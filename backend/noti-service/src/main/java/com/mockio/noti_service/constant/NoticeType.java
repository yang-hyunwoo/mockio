package com.mockio.noti_service.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NoticeType {
    EVENT("이벤트"),
    NOTICE("공지") ,
    INSPECTION("점검") ,
    ;

    private final String label;

}
