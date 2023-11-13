-- liquibase formatted sql

create table document
(
    id serial primary key,
    type    varchar(20),
    organization varchar(1000),
    date timestamp,
    description varchar(2000),
    patient varchar(100),
    status varchar(100)
);

--rollback drop table document;