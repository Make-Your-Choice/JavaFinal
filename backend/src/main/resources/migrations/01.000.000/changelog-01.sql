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

create table message_out
  (
      id serial primary key,
      topic varchar(1000),
      payload varchar(3000),
      issent bool
  );

create table message_in
(
    id serial primary key,
    payload varchar(3000),
    isaccepted bool
);
--rollback drop table document;
--rollback drop table message_out;
--rollback drop table message_in;