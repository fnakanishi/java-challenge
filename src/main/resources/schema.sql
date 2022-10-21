CREATE TABLE IF NOT EXISTS tb_user (
	`id` INTEGER PRIMARY KEY AUTO_INCREMENT,
	`username` VARCHAR(50) NOT NULL,
	`password` VARCHAR(100) NOT NULL,
	CONSTRAINT `user_uk` UNIQUE (`username`)
);

CREATE TABLE IF NOT EXISTS tb_user_role (
	`id` INTEGER PRIMARY KEY AUTO_INCREMENT,
	`user_id` INTEGER NOT NULL,
	`role_name` VARCHAR(20),
	CONSTRAINT `user_role_fk` FOREIGN KEY (`user_id`) REFERENCES tb_user (`id`),
	CONSTRAINT `user_role_uk` UNIQUE (`user_id`, `role_name`)
);

CREATE TABLE IF NOT EXISTS tb_movie (
	`id` INTEGER PRIMARY KEY AUTO_INCREMENT,
	`imdb_id` VARCHAR(100) NOT NULL,
	`title` VARCHAR(100)NOT NULL,
	`original_title` VARCHAR(100) NOT NULL,
	`full_title` VARCHAR(200),
	`launch_year` INTEGER NOT NULL,
	`image_url` VARCHAR(300),
	`release_date` DATE NOT NULL,
	`runtime` VARCHAR(10),
	`plot` VARCHAR(1000),
	`awards` VARCHAR(100),
	`directors` VARCHAR(100),
	`writers` VARCHAR(100),
	`stars` VARCHAR(100),
	`genres` VARCHAR(100),
	`languages` VARCHAR(100),
	`content_rating` VARCHAR(10),
	`favorited` INTEGER DEFAULT 0,
	CONSTRAINT movie_uk UNIQUE (`imdb_id`)
);

CREATE TABLE IF NOT EXISTS tb_favorite (
	`id` INTEGER PRIMARY KEY AUTO_INCREMENT,
	`user_id` INTEGER NOT NULL,
	`movie_id` INTEGER NOT NULL,
	CONSTRAINT `favorite_fk1` FOREIGN KEY (`user_id`) REFERENCES tb_user (`id`),
	CONSTRAINT `favorite_fk2` FOREIGN KEY (`movie_id`) REFERENCES tb_movie (`id`),
	CONSTRAINT `favorite_uk` UNIQUE (`user_id`, `movie_id`)
);