create table outbox_auth_events
(
    id              bigserial primary key,
    event_type      varchar(100) not null,
    aggregate_id    varchar(100) not null,
    idempotency_key varchar(150) not null,
    payload         jsonb        not null,
    status          VARCHAR(20)  NOT NULL DEFAULT 'NEW',
    attempt_count   int          not null default 0,
    max_attempts    int          not null default 10,
    next_attempt_at timestamptz  not null default now(),
    last_error      text null,
    locked_at       timestamptz null,
    locked_by       varchar(100) null,
    created_at      timestamptz  not null default now(),
    updated_at      timestamptz  not null default now(),
    succeeded_at    timestamptz null

  CONSTRAINT chk_outbox_auth_events_status
    CHECK (status IN ('NEW','PENDING', 'PROCESSING', 'SENT', 'DEAD','FAILED'))
);

create unique index uk_outbox_auth_events_idempotency_key
    on outbox_auth_events(idempotency_key);

create index ix_outbox_auth_events_due
    on outbox_auth_events(status, next_attempt_at);

create index ix_outbox_auth_events_created
    on outbox_auth_events(created_at);

