package com.mockio.interview_service.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.interview_service.dto.request.EnsureInterviewSettingRequest;
import com.mockio.interview_service.dto.request.UserInterviewSettingUpdateRequest;
import com.mockio.interview_service.dto.response.UserInterviewSettingReadResponse;
import com.mockio.interview_service.service.UserInterviewSettingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserInterviewSettingController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UserInterviewSettingControllerTest.TestMvcConfig.class)
class UserInterviewSettingControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean UserInterviewSettingService service;
    @MockBean MessageUtil messageUtil;

    @TestConfiguration
    static class TestMvcConfig implements WebMvcConfigurer {

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            // ⭐ "가장 앞에" 넣어서 먼저 매칭되게 함
            resolvers.add(0, new HandlerMethodArgumentResolver() {
                @Override
                public boolean supportsParameter(MethodParameter parameter) {
                    return parameter.hasParameterAnnotation(CurrentSubject.class)
                            && String.class.isAssignableFrom(parameter.getParameterType());
                }

                @Override
                public Object resolveArgument(MethodParameter parameter,
                                              org.springframework.web.method.support.ModelAndViewContainer mavContainer,
                                              org.springframework.web.context.request.NativeWebRequest webRequest,
                                              org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
                    return "kc-test-1";
                }
            });
        }
    }

    @Test
    void ensureInterviewSettingSave() throws Exception {
        when(messageUtil.getMessage("response.create")).thenReturn("created");

        EnsureInterviewSettingRequest req = new EnsureInterviewSettingRequest("kc-any");

        mockMvc.perform(post("/api/interview/v1/interview-setting/ensure")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(service).ensureInterviewSettingSave(any(EnsureInterviewSettingRequest.class));
    }

    @Test
    void getPreference() throws Exception {
        when(messageUtil.getMessage("response.read")).thenReturn("read");

        UserInterviewSettingReadResponse resp = mock(UserInterviewSettingReadResponse.class);
        when(service.getPreference("kc-test-1")).thenReturn(resp);

        mockMvc.perform(get("/api/interview/v1/me/get-preference"))
                .andExpect(status().isOk());

        verify(service).getPreference("kc-test-1");
    }

    @Test
    void updatePreference() throws Exception {
        when(messageUtil.getMessage("response.update")).thenReturn("updated");

        // 실제 직렬화 가능한 DTO로 만드는 편이 좋지만, 지금은 mock도 동작 가능
        UserInterviewSettingUpdateRequest req = mock(UserInterviewSettingUpdateRequest.class);

        mockMvc.perform(patch("/api/interview/v1/me/update-preference")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNoContent());

        verify(service).updatePreference(eq("kc-test-1"), any(UserInterviewSettingUpdateRequest.class));
    }
}