package com.mockio.auth_service.service;

import com.mockio.auth_service.client.UserProfileClient;
import com.mockio.auth_service.dto.LoginUser;
import com.mockio.auth_service.dto.response.UserAuthInfoResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserProfileClient userClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAuthInfoResponse user = userClient.getUserAuthInfo(email);

        if (user == null) {
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
        }

        return new LoginUser(
               user.id(),
                user.name(),
                user.email(),
                user.password(),
                user.failLoginCount(),
                user.status(),
                user.role()
        );
    }

}
