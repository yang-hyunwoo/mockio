CREATE TABLE interviews (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    track VARCHAR(30) NOT NULL,
    difficulty VARCHAR(30) NOT NULL,
    feedback_style VARCHAR(30) NOT NULL,
    interview_mode VARCHAR(30) NOT NULL,
    answer_time_seconds INTEGER NOT NULL,
    status VARCHAR(30) NOT NULL,
    started_at TIMESTAMPTZ NULL,
    ended_at TIMESTAMPTZ NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT ck_interviews_track CHECK (track IN ('SOFTWARE_ENGINEER','DATA','DESIGN','PRODUCT','BUSINESS','MARKETING','SALES','HR','GENERAL'))
    CONSTRAINT ck_interviews_difficulty CHECK (difficulty IN ('EASY','MEDIUM','HARD'))
    CONSTRAINT ck_interviews_feedback_style CHECK (feedback_style IN ('STRICT','COACHING','FRIENDLY'))
    CONSTRAINT ck_interviews_interview_mode CHECK (interview_mode IN ('TEXT','VOICE'))
    CONSTRAINT ck_interviews_status CHECK (status IN ('CREATED','IN_PROGRESS','COMPLETED','FAILED'))

);

CREATE INDEX  idx_interviews_user_created_at
    ON interviews (user_id, created_at DESC);