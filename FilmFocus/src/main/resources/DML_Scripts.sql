INSERT INTO filmfocus.roles (name) values ('ADMIN'), ('VENDOR'), ('USER');
INSERT INTO filmfocus.users (username, password, email, first_name, last_name, join_date) values ('admin', '$2a$12$V1nTWuB94YFrFlylh01h4evFIH04..SJ5fkuzqhLYOFOuqfk5SagO', 'admin@admin.com', 'Admin', 'Admin', now());
INSERT INTO filmfocus.users_roles (user_id, role_id) values (1, 1);
