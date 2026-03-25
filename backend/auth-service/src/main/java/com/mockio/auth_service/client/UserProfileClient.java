package com.mockio.auth_service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.dto.UserInfoResponse;
import com.mockio.auth_service.dto.request.LoginFailureRequest;
import com.mockio.auth_service.dto.request.LoginSuccessRequest;
import com.mockio.auth_service.dto.request.OauthUserRequest;
import com.mockio.auth_service.dto.response.LoginResponse;
import com.mockio.auth_service.dto.response.UserAuthInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserProfileClient {

    private final RestClient userRestClient;


    public UserAuthInfoResponse getUserAuthInfo(String email) {
        return userRestClient.get()
                .uri("/api/users/v1/internal/login/{email}", email)
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        (request, response) -> {
                            throw new UsernameNotFoundException("아이디 또는 비밀번호가 올바르지 않습니다.");
                        }
                )
                .body(UserAuthInfoResponse.class);
    }

    public UserInfoResponse userDetail(Long userId) {
        return userRestClient.get()
                .uri("/api/users/v1/internal/user-info/{userId}",userId)
                .retrieve()
                .body(UserInfoResponse.class);
    }

    public UserAuthInfoResponse oauthLogin(OauthUserRequest oauthUserRequest) {
        return userRestClient.post()
                .uri("/api/users/v1/internal/oauth-login")
                .body(oauthUserRequest)
                .retrieve()
                .body(UserAuthInfoResponse.class);
    }

    public void resetFailCount(Long userId) {
        userRestClient.patch()
                .uri("/api/users/v1/internal/login-success")
                .body(new LoginSuccessRequest(userId))
                .retrieve()
                .onStatus(status -> status.value() == 404, (req, res) -> {
                    throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
                })
                .onStatus(status -> status.is5xxServerError(), (req, res) -> {
                    throw new RuntimeException("user-service 내부 오류");
                })
                .onStatus(status -> status.is4xxClientError(), (req, res) -> {
                    throw new RuntimeException("user-service 호출 실패");
                })
                .toBodilessEntity();
    }

    public void loginFailure(String email) {
        userRestClient.patch()
                .uri("/api/users/v1/internal/login-failure")
                .body(new LoginFailureRequest(email))
                .retrieve()
                .onStatus(status -> status.value() == 404, (req, res) -> {
                    throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
                })
                .onStatus(status -> status.is5xxServerError(), (req, res) -> {
                    throw new RuntimeException("user-service 내부 오류");
                })
                .onStatus(status -> status.is4xxClientError(), (req, res) -> {
                    throw new RuntimeException("user-service 호출 실패");
                })
                .toBodilessEntity();
    }



}
