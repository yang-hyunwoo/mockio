CREATE TABLE outbox_user_events
(
    id              BIGSERIAL PRIMARY KEY,
    event_id        UUID        NOT NULL UNIQUE,
    aggregate_type  VARCHAR(50) NOT NULL, -- e.g. USER
    aggregate_id    BIGINT      NOT NULL, -- userId
    event_type      VARCHAR(50) NOT NULL, -- USER_DELETED
    payload         JSONB       NOT NULL,
    --  상태: NEW / PENDING / PROCESSING / SENT / FAILED / DEAD
    status          VARCHAR(20) NOT NULL DEFAULT 'NEW',
    -- 재시도/백오프
    attempt_count   INT         NOT NULL DEFAULT 0,
    next_attempt_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_error      TEXT,
    -- 멀티 인스턴스 중복 처리 방지/가시성
    locked_at       TIMESTAMPTZ,
    locked_by       VARCHAR(128),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    sent_at         TIMESTAMPTZ,

    CONSTRAINT chk_outbox_user_events_status
        CHECK (status IN ('NEW', 'PENDING', 'PROCESSING', 'SENT', 'DEAD', 'FAILED'))
);


-- 처리 대상 조회 최적화: (status + next_attempt_at)
CREATE INDEX idx_outbox_user_events_status_next_attempt_at
    ON outbox_user_events (status, next_attempt_at);

-- 생성 시각 기준 정렬/점검용
CREATE INDEX idx_outbox_user_events_created_at
    ON outbox_user_events (created_at);

-- 기존 인덱스 역할 포함/대체(원하시면 이것만 두고 위 created_at 인덱스는 생략 가능)
CREATE INDEX idx_outbox_user_events_status_created_at
    ON outbox_user_events (status, created_at);

-- (선택) 동일 aggregate 순서성/조회 최적화
CREATE INDEX idx_outbox_user_events_aggregate
    ON outbox_user_events (aggregate_type, aggregate_id);