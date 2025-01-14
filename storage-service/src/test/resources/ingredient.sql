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