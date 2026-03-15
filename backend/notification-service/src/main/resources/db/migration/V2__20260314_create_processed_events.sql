CREATE TABLE processed_events (
      event_id      UUID         NOT NULL,
      consumer_name VARCHAR(100) NOT NULL,
      processed_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
      PRIMARY KEY (event_id, consumer_name)
);

CREATE INDEX idx_processed_events_consumer_time
    ON processed_events (consumer_name, processed_at DESC);