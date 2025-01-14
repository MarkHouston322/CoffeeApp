create table category
(
    category_id   integer generated always as identity
        primary key,
    category_name varchar(100) not null
        unique
);