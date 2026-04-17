package com.mockio.core_service.interview.dto.request;

import com.mockio.common_core.annotation.Sanitize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record InternalQuestionBoardCreateRequest(

        @NotBlank(message = "{default.notBlank}", groups = Step1.class)
        @Sanitize(groups = Step1.class)
        @Schema(name = "제목", example = "제목 입니다.")
        String title,

        @NotBlank(message = "{default.notBlank}", groups = Step2.class)
        @Sanitize(groups = Step2.class)
        @Schema(name = "내용", example = "내용 입니다.")
        String content,

        @Schema(name = "인터뷰리스트", example = "[]")
        List<Item> interview,

        @NotNull(message = "{default.notBlank}", groups = Step3.class)
        @Schema(name = "사용자ID", example = "1")
        Long userId
) {
    public record Item(

            @NotNull(message = "{default.notBlank}", groups = Step3.class)
            @Schema(name = "인터뷰ID", example = "1")
            Long interviewId,

            @NotNull(message = "{default.notBlank}", groups = Step4.class)
            @Schema(name = "질문ID", example = "1")
            Long questionId

    ) {
    }
}
