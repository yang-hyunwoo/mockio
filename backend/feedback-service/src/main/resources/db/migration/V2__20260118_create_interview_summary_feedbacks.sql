
CREATE TABLE interview_summary_feedbacks (
    id BIGSERIAL PRIMARY KEY,
    interview_id BIGINT NOT NULL,
    summary_text TEXT NULL,
    total_score  INTEGER,
    strengths TEXT,
    improvements TEXT,
    provider VARCHAR(30),
    model VARCHAR(100),
    prompt_version VARCHAR(50),
    temperature DOUBLE PRECISION,
    generated_at TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX ux_interview_summary_feedbacks_interview_id
    ON interview_summary_feedbacks (interview_id);

CREATE INDEX ix_interview_summary_feedbacks_generated_at
    ON interview_summary_feedbacks (generated_at);