package com.mockio.noti_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mockio.common_core.annotation.Sanitize;
import com.mockio.common_core.annotation.otherValidator.ValidationGroups;
import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.noti_service.constant.NoticeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

@Getter
@Builder
public class NoticeDetailResDto {

    @Schema(description = "NoticePk",example = "1")
    private Long id;

    @Schema(description = "공지사항 제목" ,example = "출석은 어떻게")
    private String title;

    @Schema(description = "공지사항 요약" ,example = "요약입니다.")
    private String summary;

    @Schema(description = "공지사항 내용" ,example = "출석은 어떻게")
    @Sanitize(groups = Step4.class)
    private String content;

    @Schema(description = "공지사항 유형" ,example = "EVENT")
    private EnumResponse noticeType;

    @Schema(description = "등록일" ,example = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private OffsetDateTime createdAt;

    @Schema(description = "공지사항 이전글 ID",example = "1")
    private Long prevId;

    @Schema(description = "공지사항 이전글 제목",example = "안녕")
    private String prevTitle;

    @Schema(description = "공지사항 다음글 ID",example = "1")
    private Long nextId;

    @Schema(description = "공지사항 다음글 제목",example = "안녕")
    private String nextTitle;
}
