package com.mockio.noti_service.Mapper;

import com.mockio.noti_service.domain.NoticeBoard;
import com.mockio.noti_service.dto.response.NoticeDetailResDto;
import com.mockio.noti_service.dto.response.NoticePageResDto;
import com.mockio.common_spring.util.response.EnumResponse;
import jakarta.annotation.Nullable;

import java.util.List;

public class NoticeMapper {

    public static NoticePageResDto from(NoticeBoard noticeBoard) {
        return NoticePageResDto.builder()
                .id(noticeBoard.getId())
                .title(noticeBoard.getTitle())
                .noticeType(EnumResponse.of(
                        noticeBoard.getNoticeType().name(),
                        noticeBoard.getNoticeType().getLabel()
                ))
                .summary(noticeBoard.getSummary())
                .createdAt(noticeBoard.getCreatedAt())
                .build();
    }

    public static List<NoticePageResDto> fromList(List<NoticeBoard> noticeBoards) {
        return noticeBoards.stream()
                .map(NoticeMapper::from)
                .toList();
    }



    public static NoticeDetailResDto from(NoticeBoard noticeBoard,
                                          @Nullable NoticeBoard prevNoticeBoard,
                                          @Nullable NoticeBoard nextNoticeBoard
    ) {
        return NoticeDetailResDto.builder()
                .id(noticeBoard.getId())
                .title(noticeBoard.getTitle())
                .summary(noticeBoard.getSummary())
                .content(noticeBoard.getContent().getValue())
                .noticeType(EnumResponse.of(
                                noticeBoard.getNoticeType().name(),
                                noticeBoard.getNoticeType().getLabel()
                        )
                )
                .createdAt(noticeBoard.getCreatedAt())
                .prevId(prevNoticeBoard != null ? prevNoticeBoard.getId() : null)
                .prevTitle(prevNoticeBoard != null ? prevNoticeBoard.getTitle() : null)
                .nextId(nextNoticeBoard != null ? nextNoticeBoard.getId() : null)
                .nextTitle(nextNoticeBoard != null ? nextNoticeBoard.getTitle() : null)
                .build();
    }

}
