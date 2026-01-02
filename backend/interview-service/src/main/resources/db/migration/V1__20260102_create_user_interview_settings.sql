CREATE TABLE user_interview_settings (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    track VARCHAR(30) NOT NULL,
    difficulty VARCHAR(30) NOT NULL,
    feedback_style VARCHAR(30) NOT NULL,
    interview_mode VARCHAR(30) NOT NULL DEFAULT 'TEXT',
    answer_time_seconds INT NOT NULL DEFAULT 90,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    UNIQUE KEY uk_interview_setting_user_id (user_id) ,

    CONSTRAINT chk_answer_time_seconds
        CHECK (answer_time_seconds BETWEEN 30 AND 600)
);