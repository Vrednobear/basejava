SELECT *
from resume;
INSERT INTO resume (uuid, full_name)
VALUES ('123', 'Full Name');


CREATE TABLE public.resume
(
    uuid      VARCHAR PRIMARY KEY NOT NULL,
    full_name TEXT                 NOT NULL
);

CREATE TABLE public.contact
(
    id          SERIAL,
    type        VARCHAR  NOT NULL,
    value       VARCHAR  NOT NULL,
    resume_uuid VARCHAR NOT NULL,
    CONSTRAINT contact_resume_uuid_fk FOREIGN KEY (resume_uuid) REFERENCES resume (uuid) ON DELETE CASCADE,
    PRIMARY KEY (id)
);

create unique index contact__uuid_type_index
    on contact (resume_uuid, type);

DROP TABLE contact;
DROP TABLE resume;