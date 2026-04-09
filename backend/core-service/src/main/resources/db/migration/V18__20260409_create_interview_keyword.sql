CREATE TABLE interview_keyword (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    setting_id BIGINT NOT NULL,
    keyword VARCHAR(100) NOT NULL,

    CONSTRAINT fk_interview_keyword_preference
        FOREIGN KEY (setting_id)
        REFERENCES user_interview_settings (id)
        ON DELETE CASCADE
);