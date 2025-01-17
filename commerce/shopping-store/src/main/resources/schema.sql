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

INSERT INTO products(product_id, product_name, description, image_src, quantity_state, product_state, rating, product_category, price)
VALUES(1,'product1','desc1','img.png', 'ENDED', 'ACTIVE', 5, 'CONTROL', 111.1);