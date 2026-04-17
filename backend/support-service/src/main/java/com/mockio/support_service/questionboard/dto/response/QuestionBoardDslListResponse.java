package com.mockio.support_service.questionboard.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@AllArgsConstructor
@Setter
public class QuestionBoardDslListResponse {
    private Long boardId;
    private Long boardItemId;
    private String title;
    private String nickname;
    private String track;
    private String question;
    private String answerPreview;
    private int readCount;
    private Integer score;
    private Boolean scoreVisible;
    private Boolean feedbackVisible;
    private Set<String> tags;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private OffsetDateTime createdAt;
}
