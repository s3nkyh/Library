CREATE TABLE payment
(
    idempotency_key VARCHAR(100) PRIMARY KEY,
    amount          DECIMAL(10, 2) NOT NULL,
    status          VARCHAR(20)    NOT NULL
);