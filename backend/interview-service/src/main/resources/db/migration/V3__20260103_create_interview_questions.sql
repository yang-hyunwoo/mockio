CREATE TABLE interview_questions (
     id BIGSERIAL PRIMARY KEY,
     interview_id BIGINT NOT NULL,
     seq INTEGER NOT NULL,
     question_text TEXT NOT NULL,
     status VARCHAR(30) NOT NULL,
     type   VARCHAR(30) NOT NULL,
     parent_question_id BIGINT NULL,
     depth INTEGER NOT NULL,
     trigger_answer_id BIGINT NULL,
     idempotency_key VARCHAR(100) NULL,
     score INTEGER NULL,
     feedback TEXT NULL,
     prompt_tokens INTEGER NULL,
     completion_tokens INTEGER NULL,
     cost NUMERIC(10, 6) NULL,
     provider VARCHAR(30) NULL,
     model VARCHAR(100) NULL,
     prompt_version VARCHAR(50) NULL,
     temperature DOUBLE PRECISION NULL,
     generated_at TIMESTAMPTZ NULL,

     created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
     updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

     CONSTRAINT fk_interview_questions_interview
         FOREIGN KEY (interview_id)
             REFERENCES interviews (id)
             ON DELETE CASCADE,

     CONSTRAINT uk_interview_questions_interview_seq UNIQUE (interview_id, seq)
    );

-- 다음 질문 조회(READY 중 seq 최소값) 최적화
CREATE INDEX idx_interview_questions_next
    ON interview_questions (interview_id, status, seq);

-- 꼬리 질문 트리 조회 최적화
CREATE INDEX idx_interview_questions_parent
    ON interview_questions (interview_id, parent_question_id);

-- (선택) idempotencyKey를 실제로 사용할 경우 중복 방지
CREATE UNIQUE INDEX uk_interview_questions_idempo
    ON interview_questions (interview_id, idempotency_key)
    WHERE idempotency_key IS NOT NULL;