DROP TABLE IF EXISTS
payments;

CREATE TABLE IF NOT EXISTS payments (
    payment_id varchar(128) PRIMARY KEY,
    total_payment double precision,
    delivery_total double precision,
    product_total double precision,
    payment_status varchar(16)
);