-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE users
(
    id                         INTEGER PRIMARY KEY AUTOINCREMENT,
    fullname                   TEXT        not null,
    username                   TEXT unique not null,
    password                   TEXT        not null,
    consecutive_incorrect_pass INT,
    access_group               INT,
    is_active                  boolean
);

CREATE TABLE scenario
(
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    title      TEXT not null,
    creator    TEXT not null,
    difficulty INT
);

CREATE TABLE story
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    title       TEXT not null,
    description TEXT not null,
    owner       TEXT not null,
    estimate    INT,
    scenario_id INTEGER,
    FOREIGN KEY (scenario_id) REFERENCES scenario (id)
);