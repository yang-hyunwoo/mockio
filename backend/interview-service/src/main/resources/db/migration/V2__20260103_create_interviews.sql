CREATE TABLE interviews (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    track VARCHAR(30) NOT NULL,
    difficulty VARCHAR(30) NOT NULL,
    interview_feedback_style VARCHAR(30) NOT NULL,
    interview_mode VARCHAR(30) NOT NULL,

    question_gen_status VARCHAR(20) NOT NULL DEFAULT 'NONE',
    question_gen_started_at TIMESTAMPTZ NULL,
    question_gen_ended_at TIMESTAMPTZ NULL,
    question_gen_error VARCHAR(500) NULL,

    answer_time_seconds INTEGER NOT NULL,
    status VARCHAR(30) NOT NULL,
    end_reason VARCHAR(30) NULL,
    count INT NOT NULL,
    started_at TIMESTAMPTZ NULL,
    ended_at TIMESTAMPTZ NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT ck_interviews_difficulty CHECK (difficulty IN ('EASY','MEDIUM','HARD')),
    CONSTRAINT ck_interviews_interview_feedback_style CHECK (interview_feedback_style IN ('STRICT','COACHING','FRIENDLY')),
    CONSTRAINT ck_interviews_interview_mode CHECK (interview_mode IN ('TEXT','VOICE')),
    CONSTRAINT ck_interviews_status CHECK (status IN ('ACTIVE','ENDED','FAILED')),
    CONSTRAINT ck_interviews_end_reason CHECK (end_reason IN ('COMPLETED','USER_EXIT','SYSTEM_EXIT','ERROR'))
);