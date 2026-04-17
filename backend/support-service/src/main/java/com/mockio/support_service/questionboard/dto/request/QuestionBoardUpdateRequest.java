package com.mockio.support_service.questionboard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record QuestionBoardUpdateRequest(

        @NotNull(message = "{default.notBlank}", groups = Step1.class)
        @Schema(name = "면접 공유 게시판 ID", example = "1")
        Long boardId,

        @NotBlank(message = "{default.notBlank}", groups = Step2.class)
        @Schema(name = "면접 공유 게시판 제목", example = "제목입니다.")
        String title,

        @NotBlank(message = "{default.notBlank}", groups = Step3.class)
        @Schema(name = "면접 공유 게시판 내용", example = "1")
        String content,

        @Schema(name = "면접 공유 게시판 태그", example = "[]")
        Set<String> tags,

        @Schema(name = "면접 공유 게시판 ID", example = "1")
        boolean scoreVisible,

        @Schema(name = "면접 공유 게시판 ID", example = "1")
        boolean aiFeedbackVisible

) { }
