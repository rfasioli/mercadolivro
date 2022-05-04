CREATE TABLE `customer_roles` (
  `customer_id` int NOT NULL,
  `role` varchar(255) NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES customer(`id`)
)
