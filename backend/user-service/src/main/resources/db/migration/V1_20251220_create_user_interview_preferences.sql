CREATE TABLE user_interview_preferences (
    id BIGINT PRIMARY KEY,
    user_id BIGINT PRIMARY KEY,
    track VARCHAR(30) NOT NULL,
    difficulty VARCHAR(30) NOT NULL,
    feedback_style VARCHAR(30) NOT NULL,
    interview_mode VARCHAR(30) NOT NULL DEFAULT 'TEXT',
    answer_time_seconds INT NOT NULL DEFAULT 90,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_user_interview_preferences_user
        FOREIGN KEY (user_id)
            REFERENCES user_profiles (user_id)
            ON DELETE CASCADE,

    CONSTRAINT chk_answer_time_seconds
        CHECK (answer_time_seconds BETWEEN 30 AND 600)
);