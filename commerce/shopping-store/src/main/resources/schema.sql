create table if not exists products
(
    product_id       uuid default gen_random_uuid() primary key,
    product_name      varchar(255) not null,
    description       varchar(255)not null,
    image_src         varchar(255),
    price             real,
    quantity_state    varchar(255)not null,
    product_state     varchar(255) not null,
    product_category  varchar(255) not null
);