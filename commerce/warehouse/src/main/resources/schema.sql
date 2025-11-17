drop table if exists warehouse_product;



create table if not exists warehouse_product
(
    product_id uuid primary key,
    quantity   integer,
    fragile    boolean,
    width      double precision  not null,
    height     double precision  not null,
    depth      double precision  not null,
    weight     double precision  not null
);

