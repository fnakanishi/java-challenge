INSERT INTO tb_user (`username`, `password`) VALUES
('admin', 'password'),
('normalUser', 'password');

INSERT INTO tb_user_role (`user_id`, `role_name`) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER'),
(2, 'ROLE_USER');