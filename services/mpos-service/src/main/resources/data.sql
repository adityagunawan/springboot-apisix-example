INSERT INTO mpos_transactions (id, reference, amount, status, business_date) VALUES
 (1, 'TXN-0001', 150.25, 'PAID', DATE '2025-03-01'),
 (2, 'TXN-0002', 80.00, 'PENDING', DATE '2025-03-02');

ALTER TABLE mpos_transactions ALTER COLUMN id RESTART WITH 100;
