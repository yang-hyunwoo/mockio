package com.mockio.interview_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.interview_service.dto.request.GenerateInterviewQuestionsRequest;
import com.mockio.interview_service.dto.response.InterviewQuestionReadResponse;
import com.mockio.interview_service.service.InterviewQuestionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InterviewQuestionController.class)
class InterviewQuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InterviewQuestionService interviewQuestionService;

    @MockBean
    private MessageUtil messageUtil;

    @Test
    @DisplayName("POST /interviews/{id}/questions:generate - request body가 있으면 count를 전달한다")
    void generateQuestions_withBody() throws Exception {
        // given
        Long interviewId = 10L;
        String userId = "user-1";

        given(messageUtil.getMessage("response.read")).willReturn("read ok");

        InterviewQuestionReadResponse response = new InterviewQuestionReadResponse(
                List.of(
                        new InterviewQuestionReadResponse.Item(1L, 1, "Q1"),
                        new InterviewQuestionReadResponse.Item(2L, 2, "Q2")
                )
        );

        given(interviewQuestionService.generateAndSaveQuestions(eq(interviewId), eq(userId)))
                .willReturn(response);

        GenerateInterviewQuestionsRequest req = new GenerateInterviewQuestionsRequest(3);

        // when / then
        mockMvc.perform(
                        post("/api/interview/v1/interviews/{interviewId}/questions:generate", interviewId)
                                .with(jwt().jwt(j -> j.subject(userId)))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        then(interviewQuestionService).should(times(1))
                .generateAndSaveQuestions(interviewId, userId);
    }

    @Test
    @DisplayName("POST /interviews/{id}/questions:generate - body가 없으면 count=null로 전달한다")
    void generateQuestions_withoutBody() throws Exception {
        // given
        Long interviewId = 10L;
        String userId = "user-1";

        given(messageUtil.getMessage("response.read")).willReturn("read ok");

        InterviewQuestionReadResponse response = new InterviewQuestionReadResponse(
                List.of(new InterviewQuestionReadResponse.Item(1L, 1, "Q1"))
        );

        given(interviewQuestionService.generateAndSaveQuestions(eq(interviewId), eq(userId)))
                .willReturn(response);

        // when / then
        mockMvc.perform(
                        post("/api/interview/v1/interviews/{interviewId}/questions:generate", interviewId)
                                .with(jwt().jwt(j -> j.subject(userId)))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        then(interviewQuestionService).should(times(1))
                .generateAndSaveQuestions(eq(interviewId), eq(userId));

    }

    @Test
    @DisplayName("GET /interviews/{id}/questions - 질문 조회")
    void getQuestions_success() throws Exception {
        // given
        Long interviewId = 10L;
        String userId = "user-1";

        given(messageUtil.getMessage("response.read")).willReturn("read ok");

        InterviewQuestionReadResponse response = new InterviewQuestionReadResponse(
                List.of(
                        new InterviewQuestionReadResponse.Item(1L, 1, "Q1"),
                        new InterviewQuestionReadResponse.Item(2L, 2, "Q2")
                )
        );

        given(interviewQuestionService.getQuestions(interviewId)).willReturn(response);

        // when / then
        mockMvc.perform(
                        get("/api/interview/v1/interviews/{interviewId}/questions", interviewId)
                                .with(jwt().jwt(j -> j.subject(userId)))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        then(interviewQuestionService).should(times(1)).getQuestions(interviewId);
    }
}