drop table if exists order_items;
drop table if exists orders;

create table if not exists orders
(
    order_id         uuid default gen_random_uuid() primary key,
    shopping_cart_id uuid not null,
    payment_id       uuid,
    delivery_id      uuid,
    state            varchar(100),
    delivery_weight  double precision,
    delivery_volume  double precision,
    fragile          boolean,
    total_price      real,
    delivery_price   real,
    product_price    real
);

create table if not exists order_items
(
    order_id   uuid references orders (order_id),
    product_id uuid not null,
    quantity   integer
);