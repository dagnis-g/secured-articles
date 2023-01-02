--liquibase formatted sql

--changeset dagnis:1

SET MODE MySQL;

CREATE TABLE if NOT EXISTS users (
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    password VARCHAR(120) NOT NULL,
    enabled boolean NOT NULL
);

CREATE TABLE if NOT EXISTS authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES users (username)
);

INSERT INTO users(username, password, enabled) values ('admin','$2a$12$1GEyjbd0Ljlvzea3MF6BQ.P6vpbZergeInFMFVRESEW24YVOF7vMG',true);
INSERT INTO authorities(username,authority) values ('admin','ADMIN');

INSERT INTO users(username, password, enabled) values ('user','$2a$12$bi1qWWfPXCwr9x2Ri71Ziuka0G1Vbxry6Z6iTu5x1NyPA1QttLM.S',true);
INSERT INTO authorities(username,authority) values ('user','USER');

