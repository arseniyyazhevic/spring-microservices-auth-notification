INSERT INTO users (username, password, email, first_name, last_name, role)
VALUES
    ('admin', '{noop}admin123', 'admin@example.com', 'System', 'Admin', 'ADMIN'),
    ('user', '{noop}user123', 'user@example.com', 'Test', 'User', 'USER');