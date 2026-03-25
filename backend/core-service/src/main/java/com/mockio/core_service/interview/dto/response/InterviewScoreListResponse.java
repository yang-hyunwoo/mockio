package com.mockio.core_service.interview.dto.response;

import java.util.List;

public record InterviewScoreListResponse(
       List<InterviewScoreListItem> scoreList
) {}
