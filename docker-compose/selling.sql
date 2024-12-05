
create table payment_methods
(
    method_id   integer generated by default as identity
        primary key,
    method_name varchar not null
);

alter table payment_methods
    owner to postgres;

create table orders
(
    order_id                       integer generated by default as identity
        primary key,
    order_date                     timestamp         not null,
    total                          integer           not null
        constraint orders_total_check
            check (total > 0)
        constraint orders_total_check1
            check (total > 0),
    discount                       integer           not null,
    total_with_dsc                 real              not null,
    payment_id                     integer           not null
        references payment_methods,
    customer_id                    integer,
    customer_purchase_confirmation boolean,
    session_id                     integer default 0 not null,
    employee_id                    integer default 0 not null
);

alter table orders
    owner to postgres;

create table drinks_in_order
(
    drinks_in_order_id integer generated by default as identity
        primary key,
    order_id           integer               not null
        references orders,
    quantity           integer               not null
        constraint drinks_in_order_quantity_check
            check (quantity > 0),
    drink_name         varchar               not null,
    drink_price        integer               not null,
    sold_confirmation  boolean default false not null
);

alter table drinks_in_order
    owner to postgres;

