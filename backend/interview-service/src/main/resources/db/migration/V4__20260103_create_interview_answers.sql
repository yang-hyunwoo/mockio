CREATE TABLE interview_answers (
     id BIGSERIAL PRIMARY KEY,
     question_id BIGINT NOT NULL,
     attempt INTEGER NOT NULL,
     answer_text TEXT NOT NULL,
     answer_duration_seconds INTEGER NULL,
     answered_at TIMESTAMPTZ NOT NULL,
     is_current BOOLEAN NOT NULL DEFAULT TRUE,

     created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
     updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_interview_answers_question
    FOREIGN KEY (question_id)
    REFERENCES interview_questions (id)
    ON DELETE CASCADE,

    CONSTRAINT uk_interview_answers_question_attempt UNIQUE (question_id, attempt)
 );

CREATE INDEX idx_interview_answers_question_attempt
    ON interview_answers (question_id, attempt);

CREATE INDEX idx_interview_answers_question_id
    ON interview_answers (question_id);