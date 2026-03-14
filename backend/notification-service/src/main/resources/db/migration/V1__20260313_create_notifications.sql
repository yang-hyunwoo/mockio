CREATE TABLE notifications (
       id BIGSERIAL PRIMARY KEY,
       user_id BIGINT NOT NULL,
       type VARCHAR(50) NOT NULL,          -- INTERVIEW_COMPLETED, FEEDBACK_READY 등
       title VARCHAR(200) NOT NULL,
       content VARCHAR(1000) NOT NULL,
       link VARCHAR(500),                  -- 클릭 시 이동할 URL
       is_read BOOLEAN NOT NULL DEFAULT FALSE,
       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       read_at TIMESTAMP NULL
);

CREATE INDEX idx_notifications_user_read_created
    ON notifications (user_id, is_read, created_at DESC);