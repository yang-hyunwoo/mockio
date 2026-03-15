CREATE TABLE notifications (
       id BIGSERIAL PRIMARY KEY,
       user_id BIGINT NOT NULL,
       type VARCHAR(50) NOT NULL,          -- INTERVIEW_COMPLETED, FEEDBACK_READY 등
       title VARCHAR(200) NOT NULL,
       content VARCHAR(1000) NOT NULL,
       link VARCHAR(500),                  -- 클릭 시 이동할 URL
       source_event_id VARCHAR(100) NOT NULL,
       reference_type VARCHAR(50) NOT NULL,
       reference_id BIGINT NOT NULL,
       is_read BOOLEAN NOT NULL DEFAULT FALSE,
       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       read_at TIMESTAMP NULL
);

CREATE INDEX idx_notifications_user_read_created
    ON notifications (user_id, is_read, created_at DESC);

CREATE UNIQUE INDEX uk_notifications_source_event_id
    ON notifications (source_event_id);

CREATE UNIQUE INDEX uk_notifications_user_type_ref
    ON notifications (user_id, type, reference_type, reference_id);