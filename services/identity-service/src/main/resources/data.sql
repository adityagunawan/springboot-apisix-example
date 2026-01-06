INSERT INTO users (id, name, email, organization) VALUES
 (1, 'Alice', 'alice@example.com', 'Example Corp'),
 (2, 'Bob', 'bob@example.com', 'Example Corp');

ALTER TABLE users ALTER COLUMN id RESTART WITH 100;
