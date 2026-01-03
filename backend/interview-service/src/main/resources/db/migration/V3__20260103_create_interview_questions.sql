CREATE TABLE interview_questions (
    id BIGSERIAL PRIMARY KEY,
    interview_id BIGINT NOT NULL,
    seq INTEGER NOT NULL,
    question_text TEXT NOT NULL,
    provider VARCHAR(30) NULL,
    model VARCHAR(100) NULL,
    prompt_version VARCHAR(50) NULL,
    temperature DOUBLE PRECISION NULL,
    generated_at TIMESTAMPTZ NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_interview_questions_interview
    FOREIGN KEY (interview_id)
    REFERENCES interviews (id)
    ON DELETE CASCADE,

    CONSTRAINT uk_interview_questions_interview_seq UNIQUE (interview_id, seq)

);

CREATE INDEX idx_interview_questions_interview_seq
    ON interview_questions (interview_id, seq);

CREATE INDEX idx_interview_questions_interview_id
    ON interview_questions (interview_id);