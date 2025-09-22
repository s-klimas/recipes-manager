insert into
    users (email, login, status, password)
values
    ('admin@example.com', 'admin', 'active', '{noop}adminpass'),
    ('user@example.com', 'user', 'active', '{noop}userpass');

insert into
    user_role (name, description)
values
    ('ADMIN', 'pełne uprawnienia'),
    ('USER', 'podstawowe uprawnienia');

insert into
    user_roles (user_id, role_id)
values
    (1, 1),
    (2, 2);