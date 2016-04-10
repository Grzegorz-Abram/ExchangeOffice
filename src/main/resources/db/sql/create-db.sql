CREATE TABLE users (
  username VARCHAR(500) NOT NULL,
  password VARCHAR(500) NOT NULL,
  enabled boolean,
  PRIMARY KEY (username)
);

CREATE TABLE authorities (
  username VARCHAR(500) NOT NULL,
  authority VARCHAR(500) NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE wallets (
  username VARCHAR(500) NOT NULL,
  currency VARCHAR(5) NOT NULL,
  amount INT,
  PRIMARY KEY (username, currency)
);
