INSERT INTO customers (name, address, phone_number) VALUES
                                                        ('John Doe', '123 Main St', '555-1234'),
                                                        ('Jane Smith', '456 Elm St', '555-5678');

INSERT INTO deliverymen (name, phone_number) VALUES
                                                 ('Mike Johnson', '555-9876'),
                                                 ('Sarah Brown', '555-4321');

INSERT INTO items (name, price) VALUES
                                    ('Pizza', 12.99),
                                    ('Burger', 8.99),
                                    ('Salad', 6.99);

INSERT INTO orders (total_price, creation_date, customer_id, deliveryman_id) VALUES
                                                                                 (25.98, '2023-11-21', 1, 1),
                                                                                 (15.98, '2023-11-22', 2, 2);

INSERT INTO order_items (order_id, item_id) VALUES
                                                (1, 1),
                                                (1, 2),
                                                (2, 2),
                                                (2, 3);