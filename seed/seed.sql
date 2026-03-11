-- ROLES
INSERT INTO roles (id, name) VALUES
                                 (1, 'ADMIN'),
                                 (2, 'TECHNICIAN'),
                                 (3, 'EMPLOYEE');

-- USERS
INSERT INTO users (name, email, password, role_id, created_at)
VALUES
    ('Admin', 'admin@mail.com',
     '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi8C8i6Z8K1E1sC1i1sY9xX7bK9eG6K',
     1, NOW()),

    ('Technician', 'technician@mail.com',
     '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi8C8i6Z8K1E1sC1i1sY9xX7bK9eG6K',
     2, NOW()),

    ('Employee', 'employee@mail.com',
     '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi8C8i6Z8K1E1sC1i1sY9xX7bK9eG6K',
     3, NOW());