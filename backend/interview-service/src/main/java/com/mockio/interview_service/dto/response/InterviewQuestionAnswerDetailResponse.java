package com.mockio.interview_service.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class InterviewQuestionAnswerDetailResponse {
        @Schema(description = "면접_답변_pk", example = "1")
        private final Long id;
        @Schema(description = "면접_질문_pk", example = "1")
        private final Long questionId;
        @Schema(description = "답변", example = "저는 어쩌구 저쩌구")
        private final String answerText;
        @Schema(description = "멱등성키", example = "1234567890")
        private final String idempotencyKey;
        @Schema(description = "답변시간", example = "10")
        private final int answerDurationSeconds;

        @QueryProjection
        public InterviewQuestionAnswerDetailResponse(Long id,
                                                     Long questionId,
                                                     String answerText,
                                                     String idempotencyKey,
                                                     int answerDurationSeconds)
        {
                this.id = id;
                this.questionId = questionId;
                this.answerText = answerText;
                this.idempotencyKey = idempotencyKey;
                this.answerDurationSeconds = answerDurationSeconds;

        }

}