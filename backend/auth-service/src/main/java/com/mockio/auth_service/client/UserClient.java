package com.mockio.auth_service.client;

/**
 * User Service와의 내부 통신을 담당하는 Client
 *
 * - 인증/회원 관련 API 호출 전담
 * - RestClient 기반으로 HTTP 요청 수행
 * - 공통 에러 처리 전략(clientError) 적용
 *
 * 특징:
 * 1. 조회성 API → exchange() + 커스텀 에러 처리
 */

import com.mockio.auth_service.constant.UserErrorEnum;
import com.mockio.auth_service.dto.response.UserInfoResponse;
import com.mockio.auth_service.dto.request.LoginFailureRequest;
import com.mockio.auth_service.dto.request.LoginSuccessRequest;
import com.mockio.auth_service.dto.request.OauthUserRequest;
import com.mockio.auth_service.dto.response.UserAuthInfoResponse;
import com.mockio.auth_service.util.APIErrorResponse;
import com.mockio.common_core.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;

import static com.mockio.common_core.constant.CommonErrorEnum.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class UserClient {

    private final RestClient userRestClient;

    /**
     * 이메일 기반 사용자 인증 정보 조회
     *
     * @param email 사용자 이메일
     * @return 인증 정보 (비밀번호, 상태 등)
     */
    public UserAuthInfoResponse getUserAuthInfo(String email) {
        return userRestClient.get()
                .uri("/api/users/v1/internal/login/{email}", email)
                .exchange((request, response) -> {
                    clientError(response);
                    return response.bodyTo(UserAuthInfoResponse.class);
                });
    }

    /**
     * 사용자 상세 정보 조회
     *
     * @param userId 사용자 ID
     * @return 사용자 상세 정보
     */
    public UserInfoResponse userDetail(Long userId) {
        return userRestClient.get()
                .uri("/api/users/v1/internal/user-info/{userId}", userId)
                .exchange((request, response) -> {
                    clientError(response);
                    return response.bodyTo(UserInfoResponse.class);
                });
    }

    /**
     * OAuth 로그인 처리
     *
     * @param oauthUserRequest OAuth 사용자 정보
     * @return 인증 정보
     */
    public UserAuthInfoResponse oauthLogin(OauthUserRequest oauthUserRequest) {
        return userRestClient.post()
                .uri("/api/users/v1/internal/oauth-login")
                .body(oauthUserRequest)
                .exchange((request, response) -> {
                    clientError(response);
                    return response.bodyTo(UserAuthInfoResponse.class);
                });
    }

    /**
     * 로그인 실패 처리 (실패 횟수 증가)
     *
     * @param email 사용자 이메일
     */
    public void loginFailure(String email) {
        if (email == null || email.isBlank()) {
            throw new CustomApiException(ERR_001.getHttpStatus(), ERR_001, "이메일은 " + ERR_001);
        }
        userRestClient.patch()
                .uri("/api/users/v1/internal/login-failure")
                .body(new LoginFailureRequest(email))
                .retrieve()
                .onStatus(status -> status.value() == 404, (req, res) -> {
                    throw new BadCredentialsException("아이디 또는 비밀번호가 올바르지 않습니다.");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    throw new CustomApiException(ILLEGALSTATE.getHttpStatus(), ILLEGALSTATE,"user-service login fail 5xx 호출");
                })
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new CustomApiException(ERR_000.getHttpStatus(), ERR_000,"user-service login fail 4xx 호출");
                })
                .toBodilessEntity();
    }

    /**
     * 공통 에러 처리 메서드
     *
     * - user-service에서 내려준 에러 응답(JSON)을 파싱
     * - CustomApiException으로 변환하여 throw
     *
     * 처리 흐름:
     * 1. HTTP status가 error인지 확인
     * 2. APIErrorResponse로 body 파싱
     * 3. 내부 에러 코드(UserErrorEnum) 매핑
     * 4. CustomApiException으로 변환
     */
    private void clientError(RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse response) throws IOException {
        if (response.getStatusCode().isError()) {
            APIErrorResponse error = response.bodyTo(APIErrorResponse.class);
            throw new CustomApiException(
                    error.httpCode() != null
                            ? error.httpCode()
                            : response.getStatusCode().value(),
                    mapErrorCode(error.errCode()),
                    error.message() != null
                            ? error.message()
                            : error.errCodeMsg()
            );
        }
    }

    /**
     * 문자열 에러 코드를 Enum으로 변환
     *
     * - null / 빈값 → ILLEGAL_STATE
     * - 존재하지 않는 코드 → ILLEGAL_STATE
     */
    private UserErrorEnum mapErrorCode(String errCode) {
        if (errCode == null || errCode.isBlank()) {
            return UserErrorEnum.ILLEGAL_STATE;
        }
        try {
            return UserErrorEnum.valueOf(errCode);
        } catch (IllegalArgumentException e) {
            return UserErrorEnum.ILLEGAL_STATE;
        }
    }

}