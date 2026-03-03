package com.mockio.noti_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.noti_service.constant.NoticeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class NoticePageResDto {

    @Schema(description = "NoticePk",example = "1")
    private Long id;

    @Schema(description = "공지사항 제목" ,example = "출석은 어떻게")
    private String title;

    @Schema(description = "공지사항 유형" ,example = "EVENT")
    private EnumResponse noticeType;

    @Schema(description = "등록일" ,example = "yyyy-mm-dd:HH:24MM:SSS")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private OffsetDateTime createdAt;

    @Schema(description = "공지사항 요약" ,example = "내용입니다.")
    private String summary;

    private boolean pinned;

}
