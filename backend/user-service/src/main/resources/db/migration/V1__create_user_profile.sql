-- V1__create_user_profile.sql
CREATE TABLE user_profile (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  keycloak_id VARCHAR(128) NOT NULL,
  profile_image_id BIGINT,
  email VARCHAR(255),
  name VARCHAR(100),
  nickname VARCHAR(30) NOT NULL,
  bio VARCHAR(300),
  visibility VARCHAR(20) NOT NULL DEFAULT 'PUBLIC',
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  last_login_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL,
  CONSTRAINT uk_user_profile_keycloak_id UNIQUE (keycloak_id),
  CONSTRAINT uk_user_profile_nickname UNIQUE (nickname),
  CONSTRAINT ck_user_profile_visibility CHECK (visibility IN ('PUBLIC','PRIVATE')),
  CONSTRAINT ck_user_profile_status CHECK (status IN ('ACTIVE','SUSPENDED','DELETED'))
);