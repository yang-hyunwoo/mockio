package com.mockio.support_service.questionboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class QuestionBoardDslListRow {

    private Long boardId;
    private Long boardItemId;
    private String title;
    private String nickname;
    private String track;
    private int readCount;
    private String question;
    private String answerPreview;
    private Integer score;
    private Boolean scoreVisible;
    private Boolean feedbackVisible;
    private OffsetDateTime createdAt;
}
