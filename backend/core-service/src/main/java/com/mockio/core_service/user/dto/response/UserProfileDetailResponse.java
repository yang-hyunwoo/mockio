package com.mockio.core_service.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 사용자 프로필 / 면접 세팅 조회
 */

public record UserProfileDetailResponse(

        @Schema(description = "프로필 조회 DTO")
        MyPageProfileDetailResponse userProfileResponse,

        @Schema(description = "면접 세팅 조회 DTO")
        UserInterviewSettingReadResponse interviewSettingResponse

) {}
