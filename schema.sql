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
