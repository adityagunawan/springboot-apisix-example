INSERT INTO orders (id, user_id, total, order_date, status) VALUES
 (101, 1, 49.99, DATEADD('DAY', -1, CURRENT_DATE), 'PAID'),
 (102, 2, 24.50, CURRENT_DATE, 'PAID');

ALTER TABLE orders ALTER COLUMN id RESTART WITH 200;
