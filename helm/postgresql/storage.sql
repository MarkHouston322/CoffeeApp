
comment on database postgres is 'default administrative connection database';

create sequence write_off_form_write_off_id_seq;

alter sequence write_off_form_write_off_id_seq owner to postgres;

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

alter table drinks
    owner to postgres;

create table ingredients
(
    ingredient_id     integer generated always as identity
        primary key,
    name              varchar(100) not null
        unique,
    cost_per_one_kilo real         not null
        constraint ingredients_cost_per_1gram_check
            check (cost_per_one_kilo > (0)::double precision),
    quantity_in_stock real         not null
);

alter table ingredients
    owner to postgres;

create table ingredients_in_drink
(
    ingredients_in_drink_id integer generated always as identity
        primary key,
    ingredient_id           integer
        references ingredients,
    drink_id                integer
        references drinks,
    quantity                real not null
);

alter table ingredients_in_drink
    owner to postgres;

create table acceptance
(
    acceptance_id   integer generated by default as identity
        primary key,
    acceptance_date timestamp not null,
    comment         varchar,
    total           real      not null
);

alter table acceptance
    owner to postgres;

create table write_off
(
    write_off_id   integer generated by default as identity
        constraint write_off_form_pkey
            primary key,
    write_off_date timestamp not null,
    reason         varchar   not null,
    acceptance_id  integer   not null
        references acceptance,
    total          real      not null
);

alter table write_off
    owner to postgres;

alter sequence write_off_form_write_off_id_seq owned by write_off.write_off_id;

create table ingredients_in_write_off
(
    ingredients_in_write_off_id integer generated by default as identity
        primary key,
    write_off_id                integer
        references write_off,
    ingredient_id               integer
        references ingredients,
    quantity                    real not null
        constraint ingredients_in_write_off_quantity_check
            check (quantity > (0)::double precision)
    );

alter table ingredients_in_write_off
    owner to postgres;

create table ingredients_in_acceptance
(
    ingredients_in_acceptance_id integer generated by default as identity
        primary key,
    acceptance_id                integer
        references acceptance,
    ingredient_id                integer
        references ingredients,
    quantity                     real not null
        constraint ingredients_in_acceptance_quantity_check
            check (quantity > (0)::double precision)
    );

alter table ingredients_in_acceptance
    owner to postgres;

create table items
(
    item_id           integer generated by default as identity
        primary key,
    name              varchar not null
        unique,
    price             integer not null
        constraint items_price_check
            check (price > 0),
    cost_price        real    not null
        constraint items_cost_price_check
            check (cost_price > (0)::double precision),
    quantity_in_stock real    not null,
    category_id       integer
        references category,
    surcharge_ratio   real    not null
);

alter table items
    owner to postgres;

create table items_in_acceptance
(
    items_in_acceptance_id integer generated by default as identity
        primary key,
    acceptance_id          integer not null
        references acceptance,
    item_id                integer not null
        references items,
    quantity               real    not null
        constraint items_in_acceptance_quantity_check
            check (quantity > (0)::double precision)
    );

alter table items_in_acceptance
    owner to postgres;

create table items_in_write_off
(
    items_in_write_off_id integer generated by default as identity
        primary key,
    write_off_id          integer not null
        references write_off,
    item_id               integer not null
        references items,
    quantity              real    not null
        constraint items_in_write_off_quantity_check
            check (quantity > (0)::double precision)
    );

alter table items_in_write_off
    owner to postgres;

create table items_in_fridge
(
    item_in_fridge_id integer generated by default as identity
        primary key,
    item_id           integer
        references items,
    place_time        timestamp             not null,
    is_sold           boolean               not null,
    sold_date         timestamp,
    storage_time      bigint,
    expired           boolean default false not null
);

alter table items_in_fridge
    owner to postgres;

