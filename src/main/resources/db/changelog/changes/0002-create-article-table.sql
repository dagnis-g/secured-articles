--liquibase formatted sql

--changeset dagnis:2

CREATE TABLE if NOT EXISTS article (
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    title         VARCHAR(100) NOT NULL,
    author        VARCHAR(50) NOT NULL,
    content       TEXT  NOT NULL,
    publish_date  TIMESTAMP NOT NULL
);
