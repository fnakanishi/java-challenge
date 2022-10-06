INSERT INTO tb_user (`username`, `password`) VALUES
('admin', 'password'),
('normalUser', 'password');

INSERT INTO tb_user_role (`user_id`, `role_name`) VALUES
(1, 'ADMIN'),
(1, 'USER'),
(2, 'USER');