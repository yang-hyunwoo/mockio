package com.mockio.support_service.questionboard.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

public record QuestionBoardDetailResponse(

        @Schema(description = "면접 공유 ID" , example = "1")
        Long id,

        @Schema(description = "제목" , example = "제목 입니다.")
        String title,

        @Schema(description = "내용" , example = "내용 입니다.")
        String content,

        @Schema(description = "닉네임" , example = "오뚜기3분카레")
        String nickname,

        @Schema(description = "사용자확인" , example = "true")
        boolean mine,

        @Schema(description = "생성일" , example = "2026-4-16 22:20")
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm",
                timezone = "Asia/Seoul"
        )
        OffsetDateTime createdAt,

        @Schema(description = "조회수" , example = "1")
        int readCount,

        @Schema(description = "질문 내용 목록" , example = "[]")
        List<Item> interview

) {
    public record Item(

            @Schema(description = "질문 내용 ID" , example = "1")
            Long itemId,

            @Schema(description = "질문 내용" , example = "질문 내용입니다.")
            String questionText,

            @Schema(description = "작성자 답변 " , example = "작성자 답변입니다.")
            String answerText,

            @Schema(description = "질문 난이도" , example = "10")
            int seq,

            @Schema(description = "점수" , example = "10")
            Integer score,

            @Schema(description = "피드백 요약 내용" , example = "피드백 요약 내용")
            String aiFeedbackSummaryText,

            @Schema(description = "점수 공개 여부" , example = "true")
            boolean scoreVisible,

            @Schema(description = "피드백 공개 여부" , example = "false")
            boolean aiFeedbackVisible,

            @Schema(description = "태그" , example = "[]")
            Set<String> tags

    ) {

    }

}
