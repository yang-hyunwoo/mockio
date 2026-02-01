CREATE TABLE interview_feedbacks (
     id BIGSERIAL PRIMARY KEY,
     answer_id BIGINT NOT NULL,
     status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
     fail_count INT NOT NULL DEFAULT 0,
     last_error TEXT NULL,
     feedback_text TEXT NULL,
     score INTEGER NULL,
     provider VARCHAR(30) NULL,
     model VARCHAR(100) NULL,
     prompt_version VARCHAR(50) NULL,
     temperature DOUBLE PRECISION NULL,
     generated_at TIMESTAMPTZ NULL,

     created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
     updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

     CONSTRAINT chk_interview_feedbacks_status
         CHECK (status IN ('PENDING', 'SUCCEEDED', 'FAILED', 'RETRY'))
);

CREATE UNIQUE INDEX ux_interview_feedbacks_answer_id
    ON interview_feedbacks (answer_id);

CREATE INDEX idx_interview_feedbacks_answer_created_at
    ON interview_feedbacks (answer_id, created_at DESC);

CREATE INDEX idx_interview_feedbacks_status
    ON interview_feedbacks (status);