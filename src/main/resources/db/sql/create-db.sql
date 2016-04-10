CREATE TABLE users(
  username VARCHAR(50) NOT NULL,
  password VARCHAR(500) NOT NULL,
  enabled BOOLEAN NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE authorities(
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  UNIQUE INDEX (username, authority)
);

CREATE TABLE wallets(
  username VARCHAR(50) NOT NULL,
  currency VARCHAR(5) NOT NULL,
  amount INT NOT NULL,
  PRIMARY KEY (username, currency)
);
