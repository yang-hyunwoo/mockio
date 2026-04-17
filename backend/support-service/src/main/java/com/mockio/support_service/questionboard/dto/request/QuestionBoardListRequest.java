package com.mockio.support_service.questionboard.dto.request;

public record QuestionBoardListRequest(
        String value,
        String track,
        boolean scoreVisible,
        int page,
        int size
) { }
