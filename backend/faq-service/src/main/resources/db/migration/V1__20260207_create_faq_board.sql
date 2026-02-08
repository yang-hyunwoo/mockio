CREATE TABLE faq_board (
       id BIGSERIAL PRIMARY KEY,
       user_id VARCHAR(255),
       question VARCHAR(200) NOT NULL,
       answer TEXT NOT NULL,
       faq_type VARCHAR(20),
       sort INTEGER NOT NULL,
       visible BOOLEAN NOT NULL,
       deleted BOOLEAN NOT NULL,
       deleted_at TIMESTAMPTZ,
       created_by VARCHAR(255),
       updated_by VARCHAR(255),
       created_at TIMESTAMPTZ NOT NULL,
       updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_faq_board_question ON faq_board (question);

CREATE INDEX idx_faq_board_deleted ON faq_board (deleted);
