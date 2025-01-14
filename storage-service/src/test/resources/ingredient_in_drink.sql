create table category
(
    category_id   integer generated always as identity
        primary key,
    category_name varchar(100) not null
        unique
);
 insert into category(category_name) values ('Drink');

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
insert into drinks(name, price, cost_price, category_id, sold_quantity, write_off_quantity, surcharge_ratio) values ('Latte',100,50.0,1,1,1,2);

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
insert into ingredients(name, cost_per_one_kilo, quantity_in_stock) VALUES ('milk',90,2);
insert into ingredients(name, cost_per_one_kilo, quantity_in_stock) VALUES ('coffee',90,2);

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