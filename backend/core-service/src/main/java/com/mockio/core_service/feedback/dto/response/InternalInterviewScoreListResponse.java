package com.mockio.core_service.feedback.dto.response;

import java.util.List;

public record InternalInterviewScoreListResponse(
       List<InternalInterviewScoreItem> scoreList

) {
}
