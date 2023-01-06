DROP table if exists users,
    categories,
    events,
    compilations_events,
    locations,
    compilations,
    requests,
    comments,
    events_comments;

CREATE TABLE IF NOT EXISTS users
(
    id        bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    name      VARCHAR(255)                        NOT NULL,
    email     VARCHAR(255)                        NOT NULL,
    date_ban  timestamp without time zone,
    is_banned boolean                             NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT QK_USER_EMAIL unique (email)
);

CREATE TABLE IF NOT EXISTS locations
(
    id  bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    lat FLOAT,
    lon FLOAT,
    CONSTRAINT pk_location primary key (id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name VARCHAR(255)                        NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);



CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000)                       NOT NULL,
    category_id        BIGINT                              NOT NULL,
    confirmed_requests BIGINT                              NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE         NOT NULL,
    description        VARCHAR(7000)                       NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE         NOT NULL,
    initiator_id       BIGINT                              NOT NULL,
    location_id        BIGINT                              NOT NULL,
    paid               BOOLEAN                             NOT NULL,
    participant_limit  BIGINT                              NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN                             NOT NULL,
    state              VARCHAR(255)                        NOT NULL,
    title              VARCHAR(120)                        NOT NULL,
    views              BIGINT                              NOT NULL,
    CONSTRAINT pk_event primary key (id),
    CONSTRAINT fk_event_location foreign key (location_id) references locations (id),
    CONSTRAINT fk_event_category foreign key (category_id) references categories (id),
    CONSTRAINT fk_event_user foreign key (initiator_id) references users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id          bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    event_id    bigint references events (id),
    author_id   bigint references users (id),
    author_name varchar(255),
    text        varchar(3000),
    created     timestamp without time zone,
    CONSTRAINT pk_comments primary key (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    pinned BOOLEAN                             NOT NULL,
    title  VARCHAR(255)                        NOT NULL,
    CONSTRAINT pk_compilation primary key (id)
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    events_id      BIGINT references events (id) ON DELETE CASCADE       NOT NULL,
    compilation_id BIGINT references compilations (id) ON DELETE CASCADE NOT NULL,
    CONSTRAINT events_comlp_pk primary key (events_id, compilation_id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE         NOT NULL,
    event_id     BIGINT                              NOT NULL,
    requester_id BIGINT                              NOT NULL,
    status       VARCHAR(255)                        NOT NULL,
    CONSTRAINT pk_request primary key (id),
    CONSTRAINT fk_event foreign key (event_id) references events (id),
    CONSTRAINT fk_user foreign key (requester_id) references users (id)
);