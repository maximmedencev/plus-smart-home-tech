DROP TABLE IF EXISTS
products;

CREATE TABLE IF NOT EXISTS products (
    product_id varchar(128),
    fragile boolean,
    width double precision,
    height double precision,
    depth double precision,
    weight double precision,
    quantity integer
);

INSERT INTO products(product_id, fragile, width, height,depth, weight,quantity)
VALUES('first', true, 11,12,13,14,10);

INSERT INTO products(product_id, fragile, width, height,depth, weight,quantity)
VALUES('second', true, 11,12,13,14,11);

INSERT INTO products(product_id, fragile, width, height,depth, weight,quantity)
VALUES('third', true, 11,12,13,14,12);