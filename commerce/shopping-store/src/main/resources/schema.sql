DROP TABLE IF EXISTS
products;

CREATE TABLE IF NOT EXISTS products (
    product_id varchar(128) PRIMARY KEY,
    product_name varchar(128),
    description varchar(1024),
    image_src varchar(256),
    quantity_state varchar(8),
    product_state varchar(16),
    rating double precision,
    product_category varchar(16),
    price double precision
);