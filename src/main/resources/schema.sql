CREATE TABLE IF NOT EXISTS tb_user (
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    CONSTRAINT `user_uk` UNIQUE (`username`)
);

CREATE TABLE IF NOT EXISTS tb_user_role (
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `user_id` INTEGER,
    `role_name` VARCHAR(20),
    CONSTRAINT `user_role_fk` FOREIGN KEY (`user_id`) REFERENCES tb_user (`id`),
    CONSTRAINT `user_role_uk` UNIQUE (`user_id`, `role_name`)
);