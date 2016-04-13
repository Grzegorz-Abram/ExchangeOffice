INSERT INTO users (username, enabled, password) VALUES ('user', TRUE, '$2a$10$ScsraJ0KbX.vbKXVlJtE6utnI7Xr8a/Bwe1HggrG.QYBHh24D68JO');
INSERT INTO users (username, enabled, password) VALUES ('bank', FALSE, null);

INSERT INTO authorities (username, authority) VALUES ('user', 'USER');
INSERT INTO authorities (username, authority) VALUES ('bank', null);

INSERT INTO wallets (currency, username, amount) VALUES ('PLN', 'user', 1500);
INSERT INTO wallets (currency, username, amount) VALUES ('PLN', 'bank', 1000000);
INSERT INTO wallets (currency, username, amount) VALUES ('USD', 'bank', 1000000);
INSERT INTO wallets (currency, username, amount) VALUES ('EUR', 'bank', 1000000);
INSERT INTO wallets (currency, username, amount) VALUES ('CHF', 'bank', 1000000);
INSERT INTO wallets (currency, username, amount) VALUES ('RUB', 'bank', 1000000);
INSERT INTO wallets (currency, username, amount) VALUES ('CZK', 'bank', 1000000);
INSERT INTO wallets (currency, username, amount) VALUES ('GBP', 'bank', 1000000);
