CREATE TABLE processed_events (
  event_id      UUID PRIMARY KEY,
  consumer_name VARCHAR(100) NOT NULL,
  processed_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_processed_events_consumer_name
    ON processed_events (consumer_name);