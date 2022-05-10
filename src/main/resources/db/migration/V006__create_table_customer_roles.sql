CREATE TABLE customer_roles (
  customer_id INT NOT NULL,
  role VARCHAR(255) NOT NULL
);
ALTER TABLE customer_roles ADD CONSTRAINT customer_roles_fk FOREIGN KEY (customer_id) REFERENCES customer(id);

