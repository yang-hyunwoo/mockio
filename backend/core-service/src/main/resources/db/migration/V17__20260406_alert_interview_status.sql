ALTER TABLE interviews
DROP CONSTRAINT ck_interviews_status;

ALTER TABLE interviews
    ADD CONSTRAINT ck_interviews_status
        CHECK (status IN ('PENDING','GENERATING','ACTIVE','ENDED','FAILED'));