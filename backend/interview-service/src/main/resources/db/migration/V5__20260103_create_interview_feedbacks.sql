CREATE TABLE interview_feedbacks (
    id BIGSERIAL PRIMARY KEY,
    answer_id BIGINT NOT NULL,
    feedback_text TEXT NOT NULL,
    score INTEGER NULL,
    provider VARCHAR(30) NULL,
    model VARCHAR(100) NULL,
    prompt_version VARCHAR(50) NULL,
    temperature DOUBLE PRECISION NULL,
    generated_at TIMESTAMPTZ NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_interview_feedbacks_answer
    FOREIGN KEY (answer_id)
    REFERENCES interview_answers (id)
    ON DELETE CASCADE
    );

CREATE INDEX idx_interview_feedbacks_answer_id
    ON interview_feedbacks (answer_id);

CREATE INDEX idx_interview_feedbacks_answer_created_at
    ON interview_feedbacks (answer_id, created_at DESC);