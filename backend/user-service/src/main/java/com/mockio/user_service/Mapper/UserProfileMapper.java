package com.mockio.user_service.Mapper;

/**
 * UserProfileMapper
 *
 * DTO / Domain 변환  Mapper.
 *
 */

import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.dto.UserProfileDto;

public class UserProfileMapper {

    public static UserProfileDto from(UserProfile userProfile) {
        return new UserProfileDto(
                userProfile.getId(),
                userProfile.getKeycloakId(),
                userProfile.getProfileImageId(),
                userProfile.getName(),
                userProfile.getEmail(),
                userProfile.getNickname(),
                userProfile.getPhoneNumber()
        );
    }

    public static UserProfile fromKeycloakClaims(UserProfileDto userProfileDto) {
        return UserProfile.builder()
                .keycloakId(userProfileDto.keycloakId())
                .name(userProfileDto.name())
                .email(userProfileDto.email())
                .nickname(userProfileDto.nickname())
                .phoneNumber(userProfileDto.phoneNumber())
                .build();
    }

}
