CREATE TABLE IF NOT EXISTS stats
(
    id        bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    app       varchar,
    uri       varchar,
    ip        varchar,
    timestamp timestamp without time zone
);