CREATE TABLE password_reset_token (
      id BIGSERIAL PRIMARY KEY,
      user_id BIGINT NOT NULL,
      token VARCHAR(255) NOT NULL,
      expired_at TIMESTAMP NOT NULL,
      used BOOLEAN NOT NULL DEFAULT FALSE,
      used_at TIMESTAMP NULL,
      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE password_reset_token
    ADD CONSTRAINT uk_password_reset_token_token UNIQUE (token);

CREATE INDEX idx_password_reset_token_user_id
    ON password_reset_token (user_id);

CREATE INDEX idx_password_reset_token_user_used
    ON password_reset_token (user_id, used);

CREATE INDEX idx_password_reset_token_expired_at
    ON password_reset_token (expired_at);