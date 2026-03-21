package com.mockio.interview_service.dto.response;

import java.util.List;

public record WeakPointResponse(
    List<Item> weakPointList
) {
    public record Item (
            String type,
            String label,
            int averageScore,
            String message
    ){}
}
