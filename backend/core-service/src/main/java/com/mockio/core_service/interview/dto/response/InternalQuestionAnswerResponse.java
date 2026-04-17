package com.mockio.core_service.interview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record InternalQuestionAnswerResponse(

        @Schema(description = "질문리스트" , example = "[]")
        List<InternalQuestionAnswerResponse.QuestionItem> questions
) {

        public record QuestionItem(

                @Schema(description = "면접ID" , example = "1")
                Long id,

                @Schema(description = "질문순번" , example = "1")
                Integer questionOrder,

                @Schema(description = "질문" , example = "질문")
                String question,

                @Schema(description = "답변" , example = "답변")
                String answer,

                @Schema(description = "피드백" , example = "피드백")
                String feedback,

                @Schema(description = "점수" , example = "10")
                Integer score
        ) {}
}
