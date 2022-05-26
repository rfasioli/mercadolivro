CREATE TABLE purchase (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  customer_id INT NOT NULL,
  nfe VARCHAR(255),
  price DECIMAL(15, 2) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NULL);
ALTER TABLE purchase ADD CONSTRAINT customer_purchase_fk FOREIGN KEY (customer_id) REFERENCES customer(id);

CREATE TABLE purchase_book (
  purchase_id INT NOT NULL,
  book_id INT NOT NULL,
  CONSTRAINT purchase_book_pk PRIMARY KEY (purchase_id, book_id)
);
ALTER TABLE purchase_book ADD CONSTRAINT purchase_book_fk FOREIGN KEY (purchase_id) REFERENCES purchase(id);
ALTER TABLE purchase_book ADD CONSTRAINT book_purchase_fk FOREIGN KEY (book_id) REFERENCES book(id);
