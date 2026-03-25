CREATE TABLE file_group
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    domain_type  VARCHAR(50) NOT NULL,
    domain_id    BIGINT NOT NULL,
    deleted      BOOLEAN NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP(6) NOT NULL,
    updated_at   TIMESTAMP(6) NOT NULL,
    created_by   VARCHAR(255),
    updated_by   VARCHAR(255)
);


CREATE INDEX idx_file_group_domain_type_domain_id
    ON file_group (domain_type, domain_id);

CREATE INDEX idx_file_group_deleted
    ON file_group (deleted);