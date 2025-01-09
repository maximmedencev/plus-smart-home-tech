DROP TABLE IF EXISTS
carts, carts_positions;

CREATE TABLE IF NOT EXISTS carts (
    cart_id varchar(128) PRIMARY KEY,
    username varchar(128),
    active boolean
);

CREATE TABLE IF NOT EXISTS carts_positions (
    id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id varchar(128),
    cart_id varchar(128),
    quantity int,
    FOREIGN KEY (cart_id) REFERENCES carts (cart_id)
);

INSERT INTO carts(cart_id, username, active)
VALUES('newcart', 'Ivan', true);

INSERT INTO carts_positions(product_id, cart_id, quantity)
VALUES('first', 'newcart', 1);
INSERT INTO carts_positions(product_id, cart_id, quantity)
VALUES('second', 'newcart', 1);
INSERT INTO carts_positions(product_id, cart_id, quantity)
VALUES('third', 'newcart', 1);