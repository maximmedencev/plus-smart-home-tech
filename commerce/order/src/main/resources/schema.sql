DROP TABLE IF EXISTS
positions, orders;

CREATE TABLE IF NOT EXISTS orders (
    order_id varchar(128) PRIMARY KEY,
    username varchar(128),
    cart_id varchar(128),
    payment_id varchar(128),
    delivery_id varchar(128),
    state varchar(32),
    delivery_weight double precision,
    delivery_volume double precision,
    fragile boolean,
    total_price double precision,
    delivery_price double precision,
    product_price double precision,
    country varchar(128),
    city varchar(128),
    street varchar(128),
    house varchar(128),
    flat varchar(128)

);

CREATE TABLE IF NOT EXISTS positions (
    id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id varchar(128),
    order_id varchar(128),
    quantity int,
    FOREIGN KEY (order_id) REFERENCES orders (order_id)
);

