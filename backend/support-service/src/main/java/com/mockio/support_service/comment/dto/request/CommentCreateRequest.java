package com.mockio.support_service.comment.dto.request;

import com.mockio.common_core.annotation.Sanitize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record CommentCreateRequest(

        @Schema(description = "게시판 타입" , example = "questionboard")
        @NotBlank(message = "{default.notBlank}", groups = Step1.class)
        String boardType,

        @Schema(description = "게시판 ID" , example = "1")
        @NotNull(message = "{default.notBlank}", groups = Step2.class)
        Long boardId,

        @Schema(description = "댓글 내용" , example = "댓글 내용입니다.")
        @Sanitize(groups = Step3.class)
        @NotBlank(message = "{default.notBlank}", groups = Step3.class)
        String content
) { }
