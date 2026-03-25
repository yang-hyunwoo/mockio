CREATE TABLE notice_board (
    id              BIGSERIAL PRIMARY KEY,
    user_id         VARCHAR NOT NULL,
    title           VARCHAR(100) NOT NULL,
    summary         VARCHAR(100),
    content         TEXT,
    notice_type     VARCHAR(20) NOT NULL,
    notice_pin_sort VARCHAR(20) NOT NULL,
    sort            INTEGER NOT NULL,
    visible         BOOLEAN NOT NULL DEFAULT TRUE,
    deleted         BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ ,
    created_by      VARCHAR NOT NULL,
    updated_by      VARCHAR NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

CREATE INDEX idx_notice_board_title ON notice_board (title);

CREATE INDEX idx_notice_board_visible ON notice_board (visible);

CREATE INDEX idx_notice_board_deleted ON notice_board (deleted);
