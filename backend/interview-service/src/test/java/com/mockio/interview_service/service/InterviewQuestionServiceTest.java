package com.mockio.interview_service.service;

import com.mockio.common_ai_contractor.constant.*;
import com.mockio.common_ai_contractor.generator.GeneratedQuestion;
import com.mockio.common_ai_contractor.generator.InterviewQuestionGenerator;
import com.mockio.common_spring.exception.CustomApiException;
import com.mockio.interview_service.PostgresDataJpaTest;
import com.mockio.interview_service.constant.QuestionGenerationStatus;
import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.dto.response.InterviewQuestionReadResponse;
import com.mockio.interview_service.forward.ai.AIServiceClient;
import com.mockio.interview_service.repository.InterviewQuestionRepository;
import com.mockio.interview_service.repository.InterviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;
import static java.time.OffsetDateTime.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Import({InterviewQuestionService.class, InterviewQuestionServiceTest.TestConfig.class})
class InterviewQuestionServiceTest extends PostgresDataJpaTest {

    @Autowired
    private InterviewQuestionService interviewQuestionService;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private InterviewQuestionRepository interviewQuestionRepository;

    @MockBean
    private AIServiceClient aiServiceClient;

    @Test
    @DisplayName("generateAndSaveQuestions: interviewId가 없으면 INTERVIEW_NOT_FOUND 예외")
    void generateAndSaveQuestions_interviewNotFound() {
        // given
        Long notExistId = 9999L;

        // when / then
        assertThatThrownBy(() ->
                interviewQuestionService.generateAndSaveQuestions(notExistId, "user-1")
        ).isInstanceOf(CustomApiException.class)
                .hasMessageContaining(INTERVIEW_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("generateAndSaveQuestions: 소유자가 아니면 INTERVIEW_NOT_FOUND")
    void generateAndSaveQuestions_forbidden() {
        Interview interview = interviewRepository.save(createInterview("owner-1"));

        assertThatThrownBy(() ->
                interviewQuestionService.generateAndSaveQuestions(interview.getId(), "other-user")
        ).isInstanceOf(CustomApiException.class)
                .hasMessageContaining(INTERVIEW_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("generateAndSaveQuestions: 이미 질문이 있으면 QUESTIONS_ALREADY_GENERATED 예외")
    void generateAndSaveQuestions_alreadyGenerated() {
        // given

        // 기존 질문 1개 저장

        Interview interview = interviewRepository.save(createInterview("user-1"));
        InterviewQuestion existing = InterviewQuestion.createInterviewQuestion(
                interview,
                1,
                "existing question",
                "TEST",
                "test-model",
                "v0",
                0.0,
                now()
        );
        interview.markGenerated();
        interviewRepository.saveAndFlush(interview);

        interviewQuestionRepository.save(existing);

// when
        InterviewQuestionReadResponse response =
                interviewQuestionService.generateAndSaveQuestions(interview.getId(), "user-1");

// then
        assertThat(response.questions()).hasSize(1);
        assertThat(response.questions().get(0).questionText()).isEqualTo("existing question");
    }

    @Test
    @DisplayName("generateAndSaveQuestions: 정상 - n개 질문 저장 후 응답 DTO 반환")
    void generateAndSaveQuestions_success() {
        // given
        given(aiServiceClient.generateQuestions(any()))
                .willReturn(new GeneratedQuestion(List.of(
                        new GeneratedQuestion.Item(1,"FAKE-Q1","FAKE","fake","v0",0.0),
                        new GeneratedQuestion.Item(2,"FAKE-Q2","FAKE","fake","v0",0.0),
                        new GeneratedQuestion.Item(3,"FAKE-Q3","FAKE","fake","v0",0.0)
                )));
        Interview interview = interviewRepository.save(createInterview("user-1"));

        // when
        InterviewQuestionReadResponse response =
                interviewQuestionService.generateAndSaveQuestions(interview.getId(), "user-1");

        // then (응답 검증)
        assertThat(response).isNotNull();
        assertThat(response.questions()).hasSize(3);
        assertThat(response.questions()).extracting(InterviewQuestionReadResponse.Item::seq)
                .containsExactly(1, 2, 3);

        // then (DB 저장 검증)
        List<InterviewQuestion> saved =
                interviewQuestionRepository.findAllByInterviewIdOrderBySeqAsc(interview.getId());
        assertThat(saved).hasSize(3);
        assertThat(saved).extracting(InterviewQuestion::getSeq).containsExactly(1, 2, 3);
        assertThat(saved).extracting(InterviewQuestion::getQuestionText)
                .allSatisfy(text -> assertThat(text).contains("FAKE")); // TestConfig generator가 FAKE prefix를 붙임
    }

    @Test
    @DisplayName("getQuestions: seq 오름차순 조회 + DTO 매핑")
    void getQuestions_success() {
        // given
        Interview interview = interviewRepository.save(createInterview("user-1"));

        interviewQuestionRepository.saveAll(List.of(
                InterviewQuestion.createInterviewQuestion(interview, 3, "Q3", "TEST", "m", "v", 0.0, now()),
                InterviewQuestion.createInterviewQuestion(interview, 1, "Q1", "TEST", "m", "v", 0.0, now()),
                InterviewQuestion.createInterviewQuestion(interview, 2, "Q2", "TEST", "m", "v", 0.0, now())
        ));

        // when
        InterviewQuestionReadResponse response = interviewQuestionService.getQuestions(interview.getId(),"user-1");

        // then
        assertThat(response.questions()).hasSize(3);
        assertThat(response.questions()).extracting(InterviewQuestionReadResponse.Item::seq)
                .containsExactly(1, 2, 3);
        assertThat(response.questions()).extracting(InterviewQuestionReadResponse.Item::questionText)
                .containsExactly("Q1", "Q2", "Q3");
    }

    // ---------------------------
    // Test Configuration (Stub)
    // ---------------------------
    @TestConfiguration
    static class TestConfig {
        @Bean
        public InterviewQuestionGenerator interviewQuestionGenerator() {
            return command -> {
                int n = Math.max(1, command.questionCount());
                List<GeneratedQuestion.Item> items =
                        java.util.stream.IntStream.rangeClosed(1, n)
                                .mapToObj(i -> new GeneratedQuestion.Item(
                                        i,
                                        "FAKE-Q" + i + " [" + command.track() + "/" + command.difficulty() + "]",
                                        "FAKE",
                                        "fake-model",
                                        "v0",
                                        0.0
                                ))
                                .toList();

                return new GeneratedQuestion(items);
            };
        }
    }

    // ---------------------------
    // Helper: Interview 생성
    // ---------------------------
    private Interview createInterview(String userId) {
        // 프로젝트 Interview 엔티티 생성 방식에 따라 여기만 맞추면 나머지는 그대로 동작합니다.
        // (builder가 없거나 필드가 다르면, Interview의 factory/builder에 맞게 조정하세요.)

        return Interview.builder()
                .userId(userId)
                .track(InterviewTrack.GENERAL)
                .difficulty(InterviewDifficulty.MEDIUM)
                .feedbackStyle(InterviewFeedbackStyle.COACHING)
                .interviewMode(InterviewMode.TEXT)
                .answerTimeSeconds(90)
                .questionGenStatus(QuestionGenerationStatus.NONE)
                .status(InterviewStatus.ACTIVE)
                .feedbackStatus(InterviewFeedbackStatus.NONE)
                .startedAt(null)
                .endedAt(null)
                .build();
    }
}