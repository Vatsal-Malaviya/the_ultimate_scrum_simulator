-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE users
(
    id                         INT PRIMARY KEY,
    token                      TEXT not null,
    name                       TEXT unique not null ,
    pass_hash                  TEXT not null,
    consecutive_incorrect_pass INT,
    access_group               INT
);
