CREATE TABLE `book` (
  `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `title` varchar(255) NOT NULL,
  `price` decimal(19, 2) NOT NULL,
  `status` varchar(255) NOT NULL,
  `customer_id` int NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES customer(id)
)
