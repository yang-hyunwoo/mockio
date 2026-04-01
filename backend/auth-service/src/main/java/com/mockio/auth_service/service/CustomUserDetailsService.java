package com.mockio.auth_service.service;

/**
 * Spring Security 사용자 인증 정보를 조회하는 서비스 클래스
 *
 * UserDetailsService를 구현하여 로그인 시 사용자 정보를 조회한다.
 * 외부 user-service를 호출하여 인증에 필요한 사용자 정보를 가져오고,
 * 이를 LoginUser(UserDetails 구현체)로 변환하여 반환한다.
 */

import com.mockio.auth_service.client.UserClient;
import com.mockio.auth_service.constant.UserErrorEnum;
import com.mockio.auth_service.dto.LoginUser;
import com.mockio.auth_service.dto.response.UserAuthInfoResponse;

import com.mockio.common_core.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.mockio.auth_service.constant.UserErrorEnum.*;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserClient userClient;

    /**
     * 사용자 인증 정보 조회
     *
     * 입력된 이메일을 기준으로 user-service에서 사용자 정보를 조회하고,
     * 인증에 필요한 UserDetails 객체로 변환한다.
     *
     * 사용자가 존재하지 않을 경우 UsernameNotFoundException을 발생시킨다.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAuthInfoResponse user = userClient.getUserAuthInfo(email);

        if (user == null) {
            throw new CustomApiException(USER_NOT_FOUND.getHttpStatus(), USER_NOT_FOUND, USER_NOT_FOUND.getMessage());
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
