package com.mockio.user_service.service;

/**
 * UserService.
 *
 *  회원 관련 비즈니스 로직을 담당합니다.
 */

import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.dto.UserProfileDto;
import com.mockio.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * 최초 사용자 로그인 시 userProfile 등록
     * @param jwt
     */
    public void loadOrCreateFromToken(Jwt jwt) {
        String keycloakId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String username = jwt.getClaimAsString("preferred_username");
        String fullName = jwt.getClaimAsString("name");
        String phoneNumber = jwt.getClaimAsString("phone_number"); // ← Mapper를 추가해야 가져옴

        userRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    UserProfile userProfile = UserProfile.fromKeycloakClaims(
                            new UserProfileDto(null,
                                    keycloakId,
                                    null,
                                    username + fullName,
                                    email,
                                    username,
                                    phoneNumber
                            )
                    );
                    return userRepository.save(userProfile);
                });
    }



}
