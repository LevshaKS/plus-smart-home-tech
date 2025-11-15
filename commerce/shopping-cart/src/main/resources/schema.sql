drop table if exists shopping_cart_product;
drop table if exists shopping_cart_user;

create table if not exists shopping_cart_user (
    shopping_cart_id uuid default gen_random_uuid() primary key,
    username varchar(255) not null,
    active boolean not null
);

create table if not exists shopping_cart_product (
    product_id uuid not null,
    quantity integer,
    shopping_cart_id uuid references shopping_cart_user (shopping_cart_id)  on delete cascade
);