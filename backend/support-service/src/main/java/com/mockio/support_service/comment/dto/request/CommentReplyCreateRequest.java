package com.mockio.support_service.comment.dto.request;

import com.mockio.common_core.annotation.Sanitize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record CommentReplyCreateRequest(

        Long parentId,

        @Schema(description = "댓글 내용" , example = "댓글 내용입니다.")
        @Sanitize(groups = Step3.class)
        @NotBlank(message = "{default.notBlank}", groups = Step3.class)
        String content

) { }
