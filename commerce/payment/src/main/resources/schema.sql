drop table if exists payments;

create table if not exists payments
(
    payment_id     uuid default gen_random_uuid() primary key,
    order_id       uuid not null,
    products_total real,
    delivery_total real,
    total_payment  real,
    fee_total      real,
    status         varchar(30)
);