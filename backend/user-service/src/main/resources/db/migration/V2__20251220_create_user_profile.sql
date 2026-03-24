CREATE TABLE user_profiles (
   id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   user_id BIGINT NOT NULL,
   profile_image_id BIGINT NULL,
   name VARCHAR(100) NULL,
   nickname VARCHAR(30) NOT NULL,
   bio VARCHAR(300) NULL,
   visibility VARCHAR(20) NOT NULL DEFAULT 'PUBLIC',
   created_at TIMESTAMPTZ NOT NULL,
   updated_at TIMESTAMPTZ NOT NULL,

   CONSTRAINT uk_user_profiles_user_id UNIQUE (user_id),
   CONSTRAINT uk_user_profiles_nickname UNIQUE (nickname),
   CONSTRAINT ck_user_profiles_visibility CHECK (visibility IN ('PUBLIC', 'PRIVATE')),
   CONSTRAINT fk_user_profiles_user
       FOREIGN KEY (user_id) REFERENCES users(id)
);