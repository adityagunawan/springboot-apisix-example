INSERT INTO cms_items (id, title, status, publish_date) VALUES
 (1, 'Welcome Banner', 'PUBLISHED', DATE '2025-01-01'),
 (2, 'Promo Section', 'DRAFT', DATE '2025-02-15');

ALTER TABLE cms_items ALTER COLUMN id RESTART WITH 100;
