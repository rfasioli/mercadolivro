CREATE TABLE `purchase` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `nfe` VARCHAR(255),
  `price` DECIMAL(15, 2) NOT NULL,
  `created_at` TIMESTAMP NOT NULL,
  `updated_at` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE `purchase_book` (
  `purchase_id` int NOT NULL,
  `book_id` int NOT NULL,
  PRIMARY KEY (`purchase_id`, `book_id`),
  FOREIGN KEY (purchase_id) REFERENCES purchase(id),
  FOREIGN KEY (book_id) REFERENCES book(id)
);
