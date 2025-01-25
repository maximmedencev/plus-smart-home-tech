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
VALUES('product1','product1','desc1','img.png', 'ENDED', 'ACTIVE', 5, 'CONTROL', 10.0);

INSERT INTO products(product_id, product_name, description, image_src, quantity_state, product_state, rating, product_category, price)
VALUES('product2','product2','desc1','img.png', 'ENDED', 'ACTIVE', 5, 'CONTROL', 20.0);

INSERT INTO products(product_id, product_name, description, image_src, quantity_state, product_state, rating, product_category, price)
VALUES('product3','product3','desc1','img.png', 'ENDED', 'ACTIVE', 5, 'CONTROL', 30.0);