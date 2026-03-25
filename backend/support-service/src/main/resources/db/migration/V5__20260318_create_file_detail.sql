CREATE TABLE file_detail
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    file_group_id      BIGINT NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    stored_file_name   VARCHAR(255) NOT NULL,
    file_url           VARCHAR(1000) NOT NULL,
    file_size          BIGINT,
    content_type       VARCHAR(100),
    provider           VARCHAR(50),
    public_id          VARCHAR(255),
    deleted            BOOLEAN NOT NULL DEFAULT FALSE,
    created_at         TIMESTAMP(6) NOT NULL,
    updated_at         TIMESTAMP(6) NOT NULL,
    created_by         VARCHAR(255),
    updated_by         VARCHAR(255),
    CONSTRAINT fk_file_detail_file_group
        FOREIGN KEY (file_group_id) REFERENCES file_group (id)
);
CREATE INDEX idx_file_detail_file_group_id
    ON file_detail (file_group_id);

CREATE INDEX idx_file_detail_provider
    ON file_detail (provider);

CREATE INDEX idx_file_detail_public_id
    ON file_detail (public_id);

CREATE INDEX idx_file_detail_deleted
    ON file_detail (deleted);

CREATE INDEX idx_file_detail_created_by
    ON file_detail (created_by);