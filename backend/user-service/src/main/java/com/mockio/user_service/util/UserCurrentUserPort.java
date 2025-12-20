package com.mockio.user_service.util;

import com.mockio.common_security.util.CurrentUserPort;
import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCurrentUserPort implements CurrentUserPort<UserProfile> {
    private final UserProfileRepository userProfileRepository;

    @Override
    public Optional<UserProfile> findByKeycloakId(String keycloakId) {
        return userProfileRepository.findByKeycloakId(keycloakId);
    }
}
