CREATE TABLE question_tags (
   question_id BIGINT NOT NULL,
   tag VARCHAR(100) NOT NULL,

   CONSTRAINT pk_question_tags
       PRIMARY KEY (question_id, tag),

   CONSTRAINT fk_question_tags_question
       FOREIGN KEY (question_id)
           REFERENCES interview_questions (id)
           ON DELETE CASCADE
);

CREATE INDEX idx_question_tags_tag
    ON question_tags (tag);