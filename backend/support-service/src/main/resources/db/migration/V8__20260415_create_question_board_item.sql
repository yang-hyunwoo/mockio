CREATE TABLE question_board_item
(
    id                       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    question_board_id        BIGINT  NOT NULL,
    interview_id             BIGINT  NOT NULL,
    question_id              BIGINT  NOT NULL,
    question_seq             BIGINT  NOT NULL,
    question_text            TEXT    NOT NULL,
    answer_id                BIGINT  NOT NULL,
    answer_text              TEXT    NOT NULL,
    score                    INTEGER NOT NULL,
    display_order            INTEGER NOT NULL,
    ai_feedback_summary_text TEXT    NOT NULL,
    score_visible            BOOLEAN NOT NULL DEFAULT TRUE,
    ai_feedback_visible      BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_question_board_item_board
        FOREIGN KEY (question_board_id)
            REFERENCES question_board (id)
            ON DELETE CASCADE,

    CONSTRAINT uk_question_board_item_question_answer
        UNIQUE (question_board_id, question_id, answer_id),

    CONSTRAINT uk_question_board_item_display_order
        UNIQUE (question_board_id, display_order)
);



CREATE INDEX idx_qbi_board_id ON question_board_item(question_board_id);
CREATE INDEX idx_qbi_question_id ON question_board_item(question_id);
CREATE INDEX idx_qbi_interview_id ON question_board_item(interview_id);