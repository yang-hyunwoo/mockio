package com.mockio.core_service.user.dto.response;

public record MyPageProfileDetailResponse(
        String nickname,
        String email,
        Long profileImageId,
        String profileImageUrl
) {}
