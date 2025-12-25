CREATE TABLE outbox_user_events (
    id               BIGSERIAL PRIMARY KEY,
    event_id         UUID        NOT NULL UNIQUE,
    aggregate_type   VARCHAR(50) NOT NULL,   -- e.g. USER
    aggregate_id     BIGINT      NOT NULL,   -- userId
    event_type       VARCHAR(50) NOT NULL,   -- USER_DELETED
    payload          JSONB       NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'PENDING',  -- PENDING/SENT/FAILED
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    sent_at          TIMESTAMPTZ
);

CREATE INDEX idx_outbox_user_events_status_created_at
    ON outbox_user_events (status, created_at);