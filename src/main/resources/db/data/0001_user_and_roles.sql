insert into
    users (email, login, status, password)
values
    ('admin@example.com', 'admin', 'ACTIVE', '$2a$10$2CFdu//EXfmKgGtrDD97Fu1dDywy9PnZii31v8DXUaZmvO4L49jk6');

insert into
    user_role (name, description)
values
    ('ADMIN', 'full privileges'),
    ('USER', 'basic rights');

insert into
    user_roles (user_id, role_id)
values
    (1, 1);