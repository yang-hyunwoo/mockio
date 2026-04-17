package com.mockio.support_service.questionboard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record QuestionBoardDeleteRequest(

        @NotNull(message = "{default.notBlank}", groups = Step1.class)
        @Schema(name = "면접 공유 게시판 ID", example = "1")
        Long boardId

) { }
