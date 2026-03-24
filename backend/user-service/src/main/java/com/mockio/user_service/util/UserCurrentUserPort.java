package com.mockio.user_service.util;

import com.mockio.common_security.util.CurrentUserPort;
import com.mockio.user_service.domain.User;
import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.repository.UserProfileRepository;
import com.mockio.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCurrentUserPort implements CurrentUserPort<User> {
    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

}
