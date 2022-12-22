DROP table if exists users, categories, events, compilations_events, locations, compilations, requests;
CREATE TABLE IF NOT EXISTS users
(
    id    bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    name  VARCHAR                             NOT NULL,
    email VARCHAR                             NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT QK_USER_EMAIL unique (email)
);

CREATE TABLE IF NOT EXISTS locations
(
    id  bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    lat float,
    lon float,
    CONSTRAINT pk_location primary key (id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    name VARCHAR                             NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    annotation         text,
    category_id        bigint,
    confirmed_requests bigint,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    description        text,
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    initiator_id       bigint,
    location_id        bigint,
    paid               boolean,
    participant_limit  bigint,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation boolean,
    state              varchar,
    title              text,
    views              bigint,
    CONSTRAINT pk_event primary key (id),
    CONSTRAINT fk_event_location foreign key (location_id) references locations (id),
    CONSTRAINT fk_event_category foreign key (category_id) references categories (id),
    CONSTRAINT fk_event_user foreign key (initiator_id) references users (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    pinned boolean,
    title  text,
    CONSTRAINT pk_compilation primary key (id)
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    events_id bigint references events (id) ON DELETE CASCADE,
    compilation_id bigint references compilations (id) ON DELETE CASCADE,
    CONSTRAINT events_comlp_pk primary key (events_id, compilation_id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE,
    event_id     bigint,
    requester_id bigint,
    status       varchar,
    CONSTRAINT pk_request primary key (id),
    CONSTRAINT fk_event foreign key (event_id) references events (id),
    CONSTRAINT fk_user foreign key (requester_id) references users (id)
);