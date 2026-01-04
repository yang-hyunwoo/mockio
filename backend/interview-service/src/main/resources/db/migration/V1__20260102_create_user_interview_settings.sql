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

     CONSTRAINT ck_user_interview_settings_track CHECK (track IN ('SOFTWARE_ENGINEER','DATA','DESIGN','PRODUCT','BUSINESS','MARKETING','SALES','HR','GENERAL')),
     CONSTRAINT ck_user_profiles_difficulty CHECK (difficulty IN ('EASY','MEDIUM','HARD')),
     CONSTRAINT ck_user_profiles_feedback_style CHECK (feedback_style IN ('STRICT','COACHING','FRIENDLY')),
     CONSTRAINT ck_user_profiles_interview_mode CHECK (interview_mode IN ('TEXT','VOICE')),

     CONSTRAINT uk_interview_setting_user_id UNIQUE (user_id),
     CONSTRAINT chk_answer_time_seconds CHECK (answer_time_seconds BETWEEN 30 AND 600)
);