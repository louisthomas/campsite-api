-- No overlapping dateranges in Postgres. n our case [] means we include both the lower and upper bound (start and end date) in the check.
CREATE EXTENSION btree_gist;

CREATE TABLE booking (
    id                 UUID NOT NULL,
    email              VARCHAR(255),
    full_name          VARCHAR(255),
    start_date         DATE,
    end_date           DATE,
    created_date       TIMESTAMP,
    last_modified_date TIMESTAMP,
    PRIMARY KEY (id)
);

-- Don't allow two rows that have same id and overlapping (&&) date ranges.
ALTER TABLE booking
  ADD CONSTRAINT  overlapping_booking
    EXCLUDE USING gist (
        id WITH =,
        DATERANGE(start_date, end_date, '[]') WITH &&
    );