CREATE TABLE question_board
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    track      VARCHAR(255) NOT NULL,
    user_id    BIGINT       NOT NULL,
    nickname   VARCHAR(30)  NOT NULL,
    title      VARCHAR(500) NOT NULL,
    content    TEXT NULL,
    read_count INTEGER DEFAULT 0,
    deleted    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ  NOT NULL,
    updated_at TIMESTAMPTZ  NOT NULL,
    created_by BIGINT       NOT NULL,
    updated_by BIGINT       NOT NULL,
    deleted_at TIMESTAMPTZ NULL,
    deleted_by BIGINT NULL
);


CREATE INDEX idx_qb_user_id ON question_board(user_id);
CREATE INDEX idx_qb_created_at ON question_board(created_at DESC);