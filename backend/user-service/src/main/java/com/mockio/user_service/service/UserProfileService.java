package com.mockio.user_service.service;

/**
 * UserService.
 *
 *  유저 관련 비즈니스 로직을 담당합니다.
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.user_service.client.FileServiceClient;
import com.mockio.user_service.dto.response.*;
import com.mockio.user_service.mapper.UserProfileMapper;
import com.mockio.user_service.client.InterviewServiceClient;
import com.mockio.user_service.constant.UserStatus;
import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.dto.UserDeletedEvent;
import com.mockio.user_service.dto.request.ProfileSyncRequest;
import com.mockio.user_service.dto.request.UserProfileUpdateRequest;
import com.mockio.user_service.kafka.domain.OutboxUserEvent;
import com.mockio.user_service.kafka.repository.OutboxUserEventRepository;
import com.mockio.user_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.hc.client5.http.entity.mime.MultipartPart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.mockio.common_core.constant.CommonErrorEnum.ERR_012;
import static com.mockio.common_core.constant.CommonErrorEnum.ERR_500;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserProfileService {

    private final UserProfileRepository userRepository;
    private final InterviewServiceClient interviewServiceClient;
    private final OutboxUserEventRepository outboxRepository;
    private final FileServiceClient fileServiceClient;
    private final ObjectMapper objectMapper;

    private final String NAVER_NAME = "naver";

    /**
     * 최초 사용자 로그인 시 userProfile 등록
     * @param jwt
     */
    public UserProfileResponse loadOrCreateFromToken(ProfileSyncRequest profileSyncRequest) {
        String keycloakUserId = profileSyncRequest.keycloakUserId();
        String email = profileSyncRequest.email();

        String fullName = resolveFullName(profileSyncRequest);

        UserProfile userProfile = userRepository.findByKeycloakId(keycloakUserId)
                .map(existing -> {
                    existing.updateLastLoginAt();
                    interviewServiceClient.ensureInterviewSetting(existing.getId());
                    return existing;
                })
                .orElseGet(() -> createNewProfile(keycloakUserId, email, fullName));

        return UserProfileMapper.from(userProfile);
    }

    public UserIdResponse getUserId(String keycloakId) {
        UserProfile userProfile = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new CustomApiException(NOT_FOUND.value(), ERR_012, ERR_012.getMessage()));
        log.info("userProfile={}", userProfile);
        return new UserIdResponse(
                userProfile.getId(),
                userProfile.getNickname(),
                userProfile.getKeycloakId()
        );
    }

    /**
     * 유저 프로필 변경
     * @param userId
     * @param nickname
     * @param profileImage
     */
    public void updateMyProfile(Long userId , String nickname , MultipartFile profileImage) {
        Long profileImageId = null;
        UserProfile byId = findById(userId);


        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                FileUploadResponse upload = fileServiceClient.upload(profileImage, byId.getProfileImageId(), userId);
                profileImageId = upload.fileGroupId();
            } catch (IOException e) {
                throw new CustomApiException(ERR_500.getHttpStatus(), ERR_500, ERR_500.getMessage());
            }
        }
        byId.applyPatch(nickname, profileImageId);

    }

    /**
     * 유저 탈퇴
     */
    public void deleteProfile(UserProfile userProfile) {
        findByKeycloakId(userProfile.getKeycloakId()).changeStatus(UserStatus.DELETED);

        UserDeletedEvent event = UserDeletedEvent.of(userProfile.getId(), userProfile.getKeycloakId());
        JsonNode jsonNode = objectMapper.valueToTree(event);
        outboxRepository.save(OutboxUserEvent.createNew(event.eventId(), userProfile.getId(), event.eventType(), jsonNode));
    }

    /**
     * 유저 프로필 조회
     * @param
     * @return
     */
    public UserProfileDetailResponse getUserProfileDetail(Long userId) {
        UserProfile userProfile = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new CustomApiException(NOT_FOUND.value(), ERR_012, ERR_012.getMessage()));

        UserInterviewSettingReadResponse userInterviewSettingReadResponse = interviewServiceClient.interviewSetting(userId);
        UserProfileImageResponse userProfileImageResponse = null;
        if(userProfile.getProfileImageId()!= null) {
            userProfileImageResponse = fileServiceClient.getProfileImage(userId, userProfile.getProfileImageId());
        }

        return UserProfileMapper.fromDetail(userProfile,userInterviewSettingReadResponse,userProfileImageResponse);


    }

    private String resolveFullName(ProfileSyncRequest profileSyncRequest) {
        String provider = profileSyncRequest.provider();

        // 기본: name
        String name = profileSyncRequest.name();

        if (NAVER_NAME.equals(provider)) {
            String givenName = profileSyncRequest.givenName();
            if (givenName != null && !givenName.isBlank()) {
                return givenName;
            }
        }
        return name;
    }

    private UserProfile createNewProfile(String keycloakId, String email, String fullName) {
        String nickname = generateRandomNickname();
        UserProfile created = UserProfile.createUserProfile(keycloakId, email, fullName, nickname);
        created = userRepository.save(created);
        interviewServiceClient.ensureInterviewSetting(created.getId());

        created.updateLastLoginAt();
        return created;
    }

    /**
     * 랜덤 닉네임 생성
     * @return
     */
    private String generateRandomNickname() {
        String nickname;
        do {
            nickname = "user_" + RandomStringUtils.randomAlphanumeric(8).toLowerCase();
        } while (userRepository.existsByNickname(nickname));
        return nickname;
    }


    /**
     * 유저 정보 유무 조회
     * @param keycloakId
     * @return
     */
    private UserProfile findByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new CustomApiException(NOT_FOUND.value(), ERR_012, ERR_012.getMessage()));
    }

    /**
     * 유저 정보 유무 조회
     * @param userId
     * @return
     */
    private UserProfile findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException(NOT_FOUND.value(), ERR_012, ERR_012.getMessage()));
    }

}
