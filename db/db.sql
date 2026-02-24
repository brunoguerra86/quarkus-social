CREATE DATABASE IF NOT EXISTS quarkus-social;

CREATE TABLE USERS (
    id bigserial NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL
);

CREATE TABLE POSTS (
    id bigserial NOT NULL PRIMARY KEY,
    post_text varchar(150) NOT NULL,
    dateTime TIMESTAMP NOT NULL,
    user_id bigint NOT NULL references USERS(id)
);

CREATE TABLE FOLLOWERS (
    id bigserial NOT NULL PRIMARY KEY,
    user_id bigint NOT NULL references USERS(id),
    follower_id bigint NOT NULL references USERS(id)
);