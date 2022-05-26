CREATE TABLE customer (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  CONSTRAINT customer_pk PRIMARY KEY (id),
  CONSTRAINT customer_email_un UNIQUE (email)
);
