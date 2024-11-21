CREATE TABLE IF NOT EXISTS customers (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL
    );

CREATE TABLE IF NOT EXISTS deliverymen (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL
    );

CREATE TABLE IF NOT EXISTS items (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
    );

CREATE TABLE IF NOT EXISTS orders (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      total_price DECIMAL(10, 2) NOT NULL,
    creation_date DATE NOT NULL,
    customer_id INT,
    deliveryman_id INT,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (deliveryman_id) REFERENCES deliverymen(id)
    );

CREATE TABLE IF NOT EXISTS order_items (
                                           order_id INT,
                                           item_id INT,
                                           PRIMARY KEY (order_id, item_id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
    );