CREATE TABLE users
(
    id serial primary key,
    uuid varchar not null UNIQUE,
    login varchar not null UNIQUE,
    name varchar not null,
    surname varchar not null,
    email varchar not null UNIQUE,
    password varchar not null,
    role varchar not null,
    is_lock boolean DEFAULT true,
    is_enabled boolean DEFAULT false
);

CREATE TABLE content
(
    id serial primary key,
    code varchar not null,
    site varchar not null,
    lang varchar(2) not null,
    type varchar not null,
    text TEXT null,
    file_url varchar null,
    CONSTRAINT content_unique_code_lang UNIQUE (code, lang)
);

CREATE TABLE types
(
    name varchar primary key
);

CREATE TABLE languages
(
    code varchar(2) primary key,
    name varchar not null
);

CREATE TABLE timeline_posts
(
    id serial primary key,
    uuid varchar not null,
    lang varchar(2) REFERENCES "languages" (code) not null,
    name varchar not null,
    type varchar REFERENCES "types" (name) not null,
    desc_short varchar not null,
    desc_html TEXT not null,
    image_urls varchar[] not null,
    file_urls varchar[] not null,
    header_img boolean default false,
    slider char(1) default 'n',
    create_at DATE,
    start_date DATE,
    end_date DATE,
    create_by integer REFERENCES "users" (id) not null,
    CONSTRAINT timeline_posts_unique_uuid_lang UNIQUE (uuid, lang)
);

CREATE TABLE technology
(
    name varchar primary key,
    image_url varchar null
);

CREATE TABLE technology_to_post
(
    id_post integer REFERENCES "timeline_posts" (id),
    name_technology varchar REFERENCES "technology" (name)
);

CREATE TABLE it_field
(
    name varchar primary key,
    image_url varchar null,
    lang varchar(2) not null
);

CREATE TABLE field_to_post
(
    id_post integer REFERENCES "timeline_posts" (id),
    name_it_field varchar REFERENCES "it_field" (name)
);

CREATE TABLE file_data
(
    id serial PRIMARY KEY,
    uuid varchar not null UNIQUE,
    lang varchar(2) not null,
    type varchar not null,
    path varchar not null,
    is_used boolean not null DEFAULT FALSE,
    create_at DATE not null,
    CONSTRAINT file_data_unique_uuid_lang UNIQUE (uuid, lang)
)


