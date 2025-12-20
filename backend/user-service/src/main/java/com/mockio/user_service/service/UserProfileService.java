package com.mockio.user_service.service;

/**
 * UserService.
 *
 *  회원 관련 비즈니스 로직을 담당합니다.
 */

import com.mockio.user_service.Mapper.UserProfileMapper;
import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.dto.UserProfileDto;
import com.mockio.user_service.dto.response.UserProfileResponse;
import com.mockio.user_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserProfileService {

    private final UserProfileRepository userRepository;


    /**
     * 최초 사용자 로그인 시 userProfile 등록
     * @param jwt
     */
    public UserProfileResponse loadOrCreateFromToken(Jwt jwt) {
        String keycloakId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String fullName = jwt.getClaimAsString("name");

        UserProfile userProfile = userRepository.findByKeycloakId(keycloakId)
                .map(existing -> {
                    existing.updateLastLoginAt();
                    return existing;
                })
                .orElseGet(() -> {
                    String nickname = generateRandomNickname();
                    UserProfile created = UserProfile.createUserProfile(keycloakId, email, fullName, nickname);
                    created.updateLastLoginAt();
                    return userRepository.save(created);
                });

        return UserProfileMapper.from(userProfile);
    }

    private String generateRandomNickname() {
        String nickname;
        do {
            nickname = "user_" + RandomStringUtils.randomAlphanumeric(8).toLowerCase();
        } while (userRepository.existsByNickname(nickname));
        return nickname;
    }
}
