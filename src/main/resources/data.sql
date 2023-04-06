INSERT INTO tb_user (username, password) VALUES
('admin', '$2a$10$KzDaDw.VmjSggCldGTL8tOp.L8wkzZRyaXoqFLOjooM5kc3oSNviC');

INSERT INTO tb_user_role (`user_id`, `role_name`) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER');