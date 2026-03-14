package com.mockio.auth_service.controller;

import com.mockio.auth_service.client.UserProfileClient;
import com.mockio.auth_service.dto.AuthSession;
import com.mockio.auth_service.dto.VerifiedTokenClaims;
import com.mockio.auth_service.dto.response.KeycloakTokenResponse;
import com.mockio.auth_service.dto.response.SessionValidateResponse;
import com.mockio.auth_service.dto.response.UserIdResponse;
import com.mockio.auth_service.service.RefreshService;
import com.mockio.auth_service.util.AuthSessionStore;
import com.mockio.auth_service.util.JwtClaimUtil;
import com.mockio.common_core.constant.CommonErrorEnum;
import com.mockio.common_core.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/v1/public/session")
@RequiredArgsConstructor
public class SessionController {

    private final AuthSessionStore authSessionStore;
    private final RefreshService refreshService;
    private final JwtClaimUtil jwtClaimUtil;
    private final UserProfileClient userProfileClient;

    @GetMapping("/validate")
    public SessionValidateResponse validate(
            @CookieValue(name = "MOCKIO_SESSION", required = false) String sessionId
    ) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new CustomApiException(CommonErrorEnum.ERR_004.getHttpStatus(), CommonErrorEnum.ERR_004,CommonErrorEnum.ERR_004.getMessage());
        }

        AuthSession s = authSessionStore.find(sessionId)
                .orElseThrow(() -> new CustomApiException(CommonErrorEnum.ERR_004.getHttpStatus(), CommonErrorEnum.ERR_004,CommonErrorEnum.ERR_004.getMessage()));

        // 1) access 만료(또는 임박)면 refresh로 갱신
        long now = Instant.now().getEpochSecond();
        long skew = 20; // 시계 오차/레이스 방지용(10~30초 권장)
        if (s.accessExpiresAtEpochSec() <= 0 || now >= (s.accessExpiresAtEpochSec() - skew)) {
            s = refreshService.refreshBy(sessionId, s);
        }
        VerifiedTokenClaims verifiedTokenClaims = jwtClaimUtil.verifyAndExtract(s.accessToken());
        UserIdResponse userInfo = userProfileClient.getUserId(verifiedTokenClaims.keycloakUserId());
        return new SessionValidateResponse(
                userInfo.userId(),
                userInfo.keycloakUserId(),
                verifiedTokenClaims.username(),
                verifiedTokenClaims.email(),
                verifiedTokenClaims.provider(),
                verifiedTokenClaims.roles()
        );
    }

}
