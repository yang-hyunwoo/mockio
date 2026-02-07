CREATE TABLE interview_answers (
   id BIGSERIAL PRIMARY KEY,
   question_id BIGINT NOT NULL,
   attempt INTEGER NOT NULL,
   answer_text TEXT NOT NULL,
   idempotency_key VARCHAR(64),
   answer_duration_seconds INTEGER NULL,
   answered_at TIMESTAMPTZ NOT NULL,
   is_current BOOLEAN NOT NULL DEFAULT FALSE,
   followup_reason TEXT NULL,
   followup_at TIMESTAMPTZ NULL,
   created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
   updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

   CONSTRAINT fk_interview_answers_question
       FOREIGN KEY (question_id)
           REFERENCES interview_questions (id)
           ON DELETE CASCADE,

   CONSTRAINT uk_interview_answers_question_attempt UNIQUE (question_id, attempt)
);

CREATE UNIQUE INDEX uk_interview_answers_question_current
    ON interview_answers (question_id)
    WHERE is_current = TRUE;

CREATE INDEX idx_interview_answers_question_id
    ON interview_answers (question_id);

CREATE UNIQUE INDEX uk_interview_answers_question_idempotency
    ON interview_answers (question_id, idempotency_key)
    WHERE idempotency_key IS NOT NULL;