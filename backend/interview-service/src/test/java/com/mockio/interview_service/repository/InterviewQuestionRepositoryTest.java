package com.mockio.interview_service.repository;

import com.mockio.interview_service.PostgresDataJpaTest;
import com.mockio.interview_service.constant.*;
import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.domain.InterviewQuestion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InterviewQuestionRepositoryTest extends PostgresDataJpaTest {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private InterviewQuestionRepository interviewQuestionRepository;

    @Test
    @DisplayName("existsByInterviewId: 인터뷰에 질문이 하나라도 있으면 true, 없으면 false를 반환한다")
    void existsByInterviewId_works() {
        // given
        Interview interview = interviewRepository.save(createInterview("user-1"));

        // when - 질문이 없을 때
        boolean before = interviewQuestionRepository.existsByInterviewId(interview.getId());

        // then
        assertThat(before).isFalse();

        // when - 질문 저장 후
        interviewQuestionRepository.save(createQuestion(interview, 1, "Q1"));

        boolean after = interviewQuestionRepository.existsByInterviewId(interview.getId());

        // then
        assertThat(after).isTrue();
    }

    @Test
    @DisplayName("findAllByInterviewIdOrderBySeqAsc: seq 오름차순으로 정렬되어 조회된다")
    void findAllByInterviewIdOrderBySeqAsc_ordersBySeqAsc() {
        // given
        Interview interview = interviewRepository.save(createInterview("user-1"));

        // seq를 일부러 뒤섞어서 저장
        interviewQuestionRepository.saveAll(List.of(
                createQuestion(interview, 3, "Q3"),
                createQuestion(interview, 1, "Q1"),
                createQuestion(interview, 2, "Q2")
        ));

        // when
        List<InterviewQuestion> result =
                interviewQuestionRepository.findAllByInterviewIdOrderBySeqAsc(interview.getId());

        // then
        assertThat(result).hasSize(3);
        assertThat(result).extracting(InterviewQuestion::getSeq).containsExactly(1, 2, 3);
        assertThat(result).extracting(InterviewQuestion::getQuestionText).containsExactly("Q1", "Q2", "Q3");
    }

    // --- 테스트 헬퍼 메서드 ---

    private Interview createInterview(String userId) {
        return Interview.builder()
                .userId(userId)
                .track(InterviewTrack.GENERAL)
                .difficulty(InterviewDifficulty.MEDIUM)
                .feedbackStyle(FeedbackStyle.COACHING)
                .interviewMode(InterviewMode.TEXT)
                .answerTimeSeconds(90)
                .status(InterviewStatus.CREATED)
                .startedAt(null)
                .endedAt(null)
                .build();
    }

    private InterviewQuestion createQuestion(Interview interview, int seq, String text) {
        return InterviewQuestion.builder()
                .interview(interview)
                .seq(seq)
                .questionText(text)
                .provider("TEST")
                .model("test-model")
                .promptVersion("v0")
                .temperature(0.0)
                .generatedAt(OffsetDateTime.now())
                .build();
    }
}