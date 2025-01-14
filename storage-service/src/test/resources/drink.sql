create table category
(
    category_id   integer generated always as identity
        primary key,
    category_name varchar(100) not null
        unique
);

alter table category
    owner to postgres;

create table drinks
(
    drink_id           integer generated always as identity
        primary key,
    name               varchar(100) not null,
    price              integer
        constraint drinks_price_check
            check (price > 0),
    cost_price         real
        constraint drinks_cost_price_check
            check (cost_price > (0)::double precision),
    category_id        integer
        references category,
    sold_quantity      integer default 0,
    write_off_quantity integer default 0,
    surcharge_ratio    real         not null
);