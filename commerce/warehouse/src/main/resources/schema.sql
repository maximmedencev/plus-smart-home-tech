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