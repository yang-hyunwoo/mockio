CREATE TABLE comments
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    board_type       VARCHAR(30)  NOT NULL,
    board_id         BIGINT       NOT NULL,
    depth            SMALLINT     NOT NULL DEFAULT 0,
    user_id          BIGINT       NOT NULL,
    author_nickname  VARCHAR(30)  NOT NULL,
    content          VARCHAR(2000) NOT NULL,
    parent_id        BIGINT       NULL,
    deleted          BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ  NOT NULL DEFAULT now(),
    deleted_at       TIMESTAMPTZ  NULL
);

CREATE INDEX idx_comments_board ON comments(board_type, board_id);
CREATE INDEX idx_comments_parent ON comments(parent_id);
CREATE INDEX idx_comments_user ON comments(user_id);