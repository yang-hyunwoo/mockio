package com.mockio.noti_service.Mapper;

import com.mockio.noti_service.domain.NoticeBoard;
import com.mockio.noti_service.dto.response.NoticeDetailResDto;
import com.mockio.noti_service.dto.response.NoticePageResDto;

public class NoticeMapper {

    public static NoticePageResDto from(NoticeBoard noticeBoard) {
        return NoticePageResDto.builder()
                .id(noticeBoard.getId())
                .title(noticeBoard.getTitle())
                .noticeType(noticeBoard.getNoticeType())
                .createdAt(noticeBoard.getCreatedAt())
                .build();
    }


    public static NoticeDetailResDto from(NoticeBoard noticeBoard,
                                          Long prevId,
                                          Long nextId
    ) {
        return NoticeDetailResDto.builder()
                .id(noticeBoard.getId())
                .title(noticeBoard.getTitle())
                .summary(noticeBoard.getSummary())
                .content(noticeBoard.getContent().getValue())
                .noticeType(noticeBoard.getNoticeType())
                .createdAt(noticeBoard.getCreatedAt())
                .prevId(prevId)
                .nextId(nextId)
                .build();
    }
}
