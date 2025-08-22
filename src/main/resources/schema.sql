-- DROP DATABASE airbnb_clone;
CREATE DATABASE airbnb_clone DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE airbnb_clone;

-- team (공유 폴더 - 위시리스트)
CREATE TABLE `team` (
	`id`	INT	AUTO_INCREMENT PRIMARY KEY,
	`name`	VARCHAR(30)	NOT NULL
) ENGINE=InnoDB;

-- 사용자
CREATE TABLE `users` (
	`id`	INT	AUTO_INCREMENT PRIMARY KEY,
	`name`	VARCHAR(50)	NOT NULL,
	`email`	VARCHAR(255)	NOT NULL,
	`password` VARCHAR(255) NOT NULL,
    UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB;

-- 숙소
CREATE TABLE `accommodation` (
	`id`	INT	AUTO_INCREMENT PRIMARY KEY,
	-- accom_category : ROOM(방), ENTIRE(집 전체)
	`accom_category`	ENUM('ROOM','ENTIRE') NOT NULL,
	`bedrooms`	INT	NOT NULL	DEFAULT 1,
	`beds`	INT	NOT NULL	DEFAULT 1,
	`bathrooms`	INT	NOT NULL	DEFAULT 1,
	`price`	FLOAT	NOT NULL,
	-- building_category : HOUSE, APARTMENT, GUESTHOUSE, HOTEL
	`building_category`	ENUM('HOUSE','APARTMENT','GUESTHOUSE','HOTEL') NOT NULL,
	-- amenities : 비트마스크 (0~127까지 사용 가능, TINYINT로 충분)
	`amenities`	TINYINT	NOT NULL	DEFAULT 0,
    CHECK (bedrooms >= 0 AND beds >= 0 AND bathrooms >= 0 AND price >= 0)
) ENGINE=InnoDB;

-- 팀_사용자 중간 테이블
CREATE TABLE `team_users` (
	`team_id`	INT	NOT NULL,
	`user_id`	INT	NOT NULL,
    PRIMARY KEY (team_id, user_id),
	CONSTRAINT fk_team_user_team
		FOREIGN KEY (team_id) REFERENCES team (id) ON DELETE CASCADE,
	CONSTRAINT fk_team_user_user
		FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
	KEY ix_team_user_user (user_id) -- user_id로 인덱스 설정
) ENGINE=InnoDB;

-- 팀_숙소 중간 테이블
CREATE TABLE `team_accom` (
	`accom_id`	INT	NOT NULL,
	`team_id`	INT	NOT NULL,
	`like`	INT	NOT NULL	DEFAULT 0,
    PRIMARY KEY (accom_id, team_id),
	CONSTRAINT fk_team_accom_accom
		FOREIGN KEY (accom_id) REFERENCES accommodation(id) ON DELETE CASCADE,
	CONSTRAINT fk_team_accom_team
		FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE CASCADE,
	KEY ix_team_accom_team (team_id) -- team_id로 인덱스 설정
) ENGINE=InnoDB;
