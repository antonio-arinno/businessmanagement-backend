INSERT INTO companies (name, address, phone, fax, web) VALUES('ORTO COCA','Avenida xxxxx, xx, xx', '234 232 232', '333 444 444', 'ortococa@ortococa.com');
INSERT INTO companies (name) VALUES('Company 2');

INSERT INTO users (username, password, enabled, company_id, name, last_name, email) VALUES('jmccorto@gmail.com', '$2a$10$C3Uln5uqnzx/GswADURJGOIdBqYrly9731fnwKDaUdBkt/M3qvtLq', 1, 1, 'Juan Carlos', 'Coca', 'jmccorto@gmail.com');
INSERT INTO users (username, password, enabled, company_id, name, last_name, email) VALUES('monica@gmail.com', '$2a$10$RmdEsvEfhI7Rcm9f/uZXPebZVCcPC7ZXZwV51efAvMAp1rIaRAfPK', 1, 1, 'Monica', 'Apellido', 'monica@gmail.com');
INSERT INTO users (username, password, enabled, company_id, name, last_name, email) VALUES('antonio.arinno@gmail.com', '$2a$10$ZmsZpeO0Xq9GvEvaOYZ42OIXD7f9Nn8fdLrMPXUu0k2fgV/XXqmhW', 1, 1, 'Antonio', 'Ariño', 'antonio.arinno@gmail.com');



INSERT INTO roles (name) VALUES('ROLE_ADMIN');
INSERT INTO roles (name) VALUES('ROLE_USER');

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (3, 1);

INSERT INTO customers (code, create_at, name, company_id) VALUES(1, now(), 'Cliente 1', 1);
INSERT INTO customers (code, create_at, name, company_id) VALUES(2, now(), 'Cliente 2', 2);
INSERT INTO customers (code, create_at, name, company_id) VALUES(3, now(), 'Cliente 3', 1);
INSERT INTO customers (code, create_at, name, company_id) VALUES(4, now(), 'Cliente 4', 2);


INSERT INTO products (code, create_at, description, price, company_id) VALUES (1, NOW(),'producto 1', 100, 1);
INSERT INTO products (code, create_at, description, price, company_id) VALUES (1, NOW(),'producto 2', 100, 2);
INSERT INTO products (code, create_at, description, price, company_id) VALUES (2, NOW(),'producto 3', 300, 1);
INSERT INTO products (code, create_at, description, price, company_id) VALUES (2, NOW(),'producto 4', 250, 2);


