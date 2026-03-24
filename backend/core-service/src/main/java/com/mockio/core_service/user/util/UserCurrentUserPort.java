package com.mockio.core_service.user.util;

import com.mockio.common_security.util.CurrentUserPort;
import com.mockio.core_service.user.domain.User;
import com.mockio.core_service.user.repository.UserRepository;
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
