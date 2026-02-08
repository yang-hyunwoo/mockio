CREATE TABLE inquiry_board(
    id               BIGSERIAL PRIMARY KEY,
    inquiry_type     VARCHAR(20)  NOT NULL,
    question_user_id VARCHAR      NOT NULL,
    question_title   VARCHAR(500) NOT NULL,
    question_content TEXT,
    answer_user_id   VARCHAR,
    answer_content   TEXT,
    answer_at        TIMESTAMPTZ,
    created_by       VARCHAR      NOT NULL,
    updated_by       VARCHAR      NOT NULL,
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_inquiry_board_title ON inquiry_board (question_title);

CREATE INDEX idx_inquiry_board_inquiry_type ON notice_board (inquiry_type);
