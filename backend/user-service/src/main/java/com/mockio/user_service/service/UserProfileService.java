package com.mockio.user_service.service;

/**
 * UserService.
 *
 *  유저 관련 비즈니스 로직을 담당합니다.
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_spring.exception.CustomApiException;
import com.mockio.user_service.Mapper.UserProfileMapper;
import com.mockio.user_service.client.InterviewServiceClient;
import com.mockio.user_service.constant.UserStatus;
import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.dto.UserDeletedEvent;
import com.mockio.user_service.dto.request.UserProfileUpdateRequest;
import com.mockio.user_service.dto.response.UserProfileResponse;
import com.mockio.user_service.kafka.domain.OutboxUserEvent;
import com.mockio.user_service.kafka.repository.OutboxUserEventRepository;
import com.mockio.user_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;

import static com.mockio.common_spring.constant.CommonErrorEnum.ERR_012;
import static com.mockio.common_spring.constant.CommonErrorEnum.ILLEGALSTATE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserProfileService {

    private final UserProfileRepository userRepository;
    private final InterviewServiceClient interviewServiceClient;
    private final OutboxUserEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    private final String NAVER_NAME = "naver";

    /**
     * 최초 사용자 로그인 시 userProfile 등록
     * @param jwt
     */
    public UserProfileResponse loadOrCreateFromToken(Jwt jwt) {
        String keycloakId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");

        String fullName = resolveFullName(jwt);

        UserProfile userProfile = userRepository.findByKeycloakId(keycloakId)
                .map(existing -> {
                    existing.updateLastLoginAt();
                    return existing;
                })
                .orElseGet(() -> createNewProfile(keycloakId, email, fullName));

        return UserProfileMapper.from(userProfile);
    }

    /**
     * 유저 프로필 변경
     * @param userProfile
     * @param userProfileUpdateRequest
     */
    public void updateMyProfile(UserProfile userProfile, UserProfileUpdateRequest userProfileUpdateRequest) {
        findByKeycloakId(userProfile.getKeycloakId()).applyPatch(
                userProfileUpdateRequest.nickname(),
                userProfileUpdateRequest.profileImageId(),
                userProfileUpdateRequest.bio(),
                userProfileUpdateRequest.visibility()
        );
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

    private String resolveFullName(Jwt jwt) {
        String provider = jwt.getClaimAsString("provider");

        // 기본: name
        String name = jwt.getClaimAsString("name");

        if (NAVER_NAME.equals(provider)) {
            String givenName = jwt.getClaimAsString("given_name");
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
        interviewServiceClient.ensureInterviewSetting(keycloakId);

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


}
