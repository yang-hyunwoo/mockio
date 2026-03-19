package com.mockio.user_service.dto.response;

public record MyPageProfileDetailResponse(
        String nickname,
        String email,
        Long profileImageId,
        String profileImageUrl
) {}
