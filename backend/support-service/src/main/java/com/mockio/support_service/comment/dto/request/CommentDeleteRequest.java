package com.mockio.support_service.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record CommentDeleteRequest(

        @Schema(description = "게시판 타입" , example = "questionboard")
        @NotBlank(message = "{default.notBlank}", groups = Step1.class)
        String boardType,

        @Schema(description = "댓글 ID" , example = "1")
        @NotNull(message = "{default.notBlank}", groups = Step2.class)
        Long id,

        @Schema(description = "뎁스" , example = "1")
        @NotBlank(message = "{default.notBlank}", groups = Step3.class)
        int depth,

        @Schema(description = "부모 댓글 ID" , example = "1")
        Long parentId

) { }
