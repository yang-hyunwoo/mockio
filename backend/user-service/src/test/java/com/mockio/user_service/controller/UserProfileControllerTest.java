package com.mockio.user_service.controller;

import com.mockio.common_spring.util.MessageUtil;
import com.mockio.user_service.TestSecurityPermitAllConfig;
import com.mockio.user_service.dto.response.UserProfileResponse;
import com.mockio.user_service.service.UserProfileService;
import com.mockio.user_service.util.CurrentUserWiringConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UserProfileController.class)
@Import(TestSecurityPermitAllConfig.class)
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserProfileService userProfileService;

    @MockBean
    private MessageUtil messageUtil;

    @MockBean
    private CurrentUserWiringConfig currentUserWiringConfig;

    @Test
    @DisplayName("내 프로필 동기화 요청 시 200 OK와 응답 스펙을 반환한다")
    void shouldReturnOkResponseWhenSyncMyProfile() throws Exception {
        // given
        given(messageUtil.getMessage("response.read")).willReturn("조회 성공");

        UserProfileResponse response = new UserProfileResponse(
                1L,
                "user_test",
                "홍길동",
                "a@b.com",
                null,
                null,
                null,
                null,
                OffsetDateTime.now()
        );

        given(userProfileService.loadOrCreateFromToken(any(Jwt.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/users/v1/me/sync")
                        .with(jwt().jwt(j -> j
                                .subject("kc-123")
                                .claim("email", "a@b.com")
                                .claim("preferred_username", "user1")
                                .claim("name", "홍길동")
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                // Response.ok 스펙 검증 (프로젝트 Response 포맷 기준)
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.httpCode").value(200))
                .andExpect(jsonPath("$.message").value("조회 성공"))
                .andExpect(jsonPath("$.data").exists())

                // data 내부도 최소 1~2개는 찍어두는 게 좋습니다.
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email").value("a@b.com"))
                .andExpect(jsonPath("$.data.nickname").value("user_test"));

        // (선택) 컨트롤러가 Jwt를 서비스로 전달했는지까지 검증
        ArgumentCaptor<Jwt> captor = ArgumentCaptor.forClass(Jwt.class);
        then(userProfileService).should().loadOrCreateFromToken(captor.capture());
        assertThat(captor.getValue().getSubject()).isEqualTo("kc-123");
    }

    @Test
    @DisplayName("인증 주체가 없으면 401/403이 발생한다")
    void shouldFailWhenJwtIsMissing() throws Exception {
        mockMvc.perform(post("/api/users/v1/me/sync")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("비로그인 사용자 - me는 null")
    void publicPage_anonymousUser() throws Exception {
        mockMvc.perform(get("/api/users/v1/public/me/public-page"))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("로그인 사용자 - me는 null 아님")
    void publicPage_authenticatedUser() throws Exception {
        mockMvc.perform(get("/api/users/v1/public/me/public-page")
                                .with(jwt().jwt(jwt -> jwt.subject("keycloak-user-123")))
                )
                .andExpect(status().isOk());
    }
}
