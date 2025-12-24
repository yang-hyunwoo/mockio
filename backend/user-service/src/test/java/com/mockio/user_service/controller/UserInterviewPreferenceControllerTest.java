package com.mockio.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.user_service.TestCurrentUserResolverConfig;
import com.mockio.user_service.constant.FeedbackStyle;
import com.mockio.user_service.constant.InterviewDifficulty;
import com.mockio.user_service.constant.InterviewMode;
import com.mockio.user_service.constant.InterviewTrack;
import com.mockio.user_service.dto.request.UserInterviewPreferenceUpdateRequest;
import com.mockio.user_service.dto.response.UserInterviewPreferenceReadResponse;
import com.mockio.user_service.service.UserInterviewPreferenceService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserInterviewPreferenceController.class)
@Import({CurrentUserWiringConfig.class , TestCurrentUserResolverConfig.class}) // @CurrentUser 바인딩을 위한 ArgumentResolver 설정
class UserInterviewPreferenceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserInterviewPreferenceService userInterviewPreferenceService;

    @MockBean
    MessageUtil messageUtil;

    @MockBean
    private CurrentUserWiringConfig currentUserWiringConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/users/v1/me/get-preference: 성공 시 service.getPreference 호출 + 200 응답")
    void getPreference_success() throws Exception {
        // given
        String keycloakId = "kc-123";
        given(messageUtil.getMessage("response.read")).willReturn("read");

        UserInterviewPreferenceReadResponse response =
                new UserInterviewPreferenceReadResponse(
                        10L,
                        EnumResponse.of(InterviewTrack.GENERAL, "GENERAL"),
                        EnumResponse.of(InterviewDifficulty.EASY, "EASY"),
                        EnumResponse.of(FeedbackStyle.COACHING, "COACHING"),
                        EnumResponse.of(InterviewMode.TEXT, "TEXT"),
                        0
                );

        given(userInterviewPreferenceService.getPreference(keycloakId)).willReturn(response);

        // when & then
        mockMvc.perform(
                        get("/api/users/v1/me/get-preference")
                                .with(jwt()) // 인증 통과용(프로젝트가 Bearer 필요)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // Response<T> 포맷에 맞춘 최소 검증(필드명은 실제 Response 구조에 맞춰 조정)
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.httpCode").value(200))
                .andExpect(jsonPath("$.message").value("read"))
                .andExpect(jsonPath("$.data.track.code").value("GENERAL"))
                .andExpect(jsonPath("$.data.difficulty.code").value("EASY"))
                .andExpect(jsonPath("$.data.feedbackStyle.code").value("COACHING"))
                .andExpect(jsonPath("$.data.interviewMode.code").value("TEXT"));

        then(userInterviewPreferenceService).should(times(1)).getPreference(eq(keycloakId));
        then(messageUtil).should(times(1)).getMessage("response.read");
    }

    @Test
    @DisplayName("GET /api/users/v1/me/get-preference: service에서 예외 발생 시 5xx로 전파된다")
    void getPreference_serviceThrows_propagatesError() throws Exception {
        // given
        given(messageUtil.getMessage("response.read")).willReturn("read");
        willThrow(new RuntimeException("boom"))
                .given(userInterviewPreferenceService)
                .getPreference(anyString());

        // when & then
        mockMvc.perform(
                        get("/api/users/v1/me/get-preference")
                                .with(jwt())
                )
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("PATCH /api/users/v1/me/update-preference: 성공 시 service.updatePreference 호출 + 200 응답")
    void updatePreference_success() throws Exception {
        // given
        String keycloakId = "kc-123";
        given(messageUtil.getMessage("response.update")).willReturn("updated");
        UserInterviewPreferenceUpdateRequest userInterviewPreferenceUpdateRequest = new UserInterviewPreferenceUpdateRequest(InterviewTrack.GENERAL, InterviewDifficulty.EASY, FeedbackStyle.COACHING, InterviewMode.TEXT, 0);
        ;
        // when
        mockMvc.perform(
                patch("/api/users/v1/me/update-preference")
                        .with(csrf())
                        .with(jwt().jwt(Jwt.withTokenValue("t")
                                .header("alg", "none")
                                .claim("sub", "kc-123") // @CurrentUser가 읽는 claim에 맞춰야 함
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInterviewPreferenceUpdateRequest))
        ).andExpect(status().isOk());

        // then
        ArgumentCaptor<String> keycloakCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UserInterviewPreferenceUpdateRequest> reqCaptor =
                ArgumentCaptor.forClass(UserInterviewPreferenceUpdateRequest.class);

        then(userInterviewPreferenceService).should(times(1))
                .updatePreference(keycloakCaptor.capture(), reqCaptor.capture());

        assertThat(keycloakCaptor.getValue()).isEqualTo(keycloakId);
        assertThat(reqCaptor.getValue()).isNotNull();
        assertThat(reqCaptor.getValue().track().name()).isEqualTo("GENERAL");
    }

    @Test
    @DisplayName("PATCH /api/users/v1/me/update-preference: service에서 예외 발생 시 에러 응답으로 전파된다")
    void updatePreference_serviceThrows_propagatesError() throws Exception {
        // given
        willThrow(new RuntimeException("boom"))
                .given(userInterviewPreferenceService)
                .updatePreference(anyString(), any(UserInterviewPreferenceUpdateRequest.class));

        // when / then
        mockMvc.perform(
                        patch("/api/users/v1/me/update-preference")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .with(jwt().jwt(Jwt.withTokenValue("t")
                                        .header("alg", "none")
                                        .claim("sub", "kc-123") // @CurrentUser가 읽는 claim에 맞춰야 함
                                        .build()))
                                .param("answerTimeSeconds", "60")
                )
                .andExpect(status().is5xxServerError());
    }
}