CREATE TABLE question_board_item_tags
(
    board_item_id BIGINT       NOT NULL,
    tag           VARCHAR(100) NOT NULL,

    CONSTRAINT pk_question_board_item_tags
        PRIMARY KEY (board_item_id, tag),

    CONSTRAINT fk_question_board_item_tag_item
        FOREIGN KEY (board_item_id)
            REFERENCES question_board_item (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_question_item_tags_tag
    ON question_board_item_tags(tag);
