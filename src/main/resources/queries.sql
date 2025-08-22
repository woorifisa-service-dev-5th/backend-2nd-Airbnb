-- ==========================
-- team (팀 5개 생성)
-- ==========================
INSERT INTO team (name) VALUES
('TEAM1'),
('TEAM2'),
('TEAM3'),
('TEAM4'),
('TEAM5');

-- ==========================
-- users (30명 생성, email은 @fisa.com 통일)
-- ==========================
INSERT INTO users (name, email, password) VALUES
('Alice Johnson', 'alice1@fisa.com', 'pass123'),
('Bob Smith', 'bob2@fisa.com', 'pass123'),
('Charlie Brown', 'charlie3@fisa.com', 'pass123'),
('David Lee', 'david4@fisa.com', 'pass123'),
('Emma Wilson', 'emma5@fisa.com', 'pass123'),
('Frank Harris', 'frank6@fisa.com', 'pass123'),
('Grace Miller', 'grace7@fisa.com', 'pass123'),
('Henry Davis', 'henry8@fisa.com', 'pass123'),
('Isabella Clark', 'isabella9@fisa.com', 'pass123'),
('Jack Lewis', 'jack10@fisa.com', 'pass123'),
('Karen Young', 'karen11@fisa.com', 'pass123'),
('Liam Hall', 'liam12@fisa.com', 'pass123'),
('Mia Allen', 'mia13@fisa.com', 'pass123'),
('Noah Wright', 'noah14@fisa.com', 'pass123'),
('Olivia King', 'olivia15@fisa.com', 'pass123'),
('Paul Scott', 'paul16@fisa.com', 'pass123'),
('Quinn Green', 'quinn17@fisa.com', 'pass123'),
('Rachel Adams', 'rachel18@fisa.com', 'pass123'),
('Sam Baker', 'sam19@fisa.com', 'pass123'),
('Tina Nelson', 'tina20@fisa.com', 'pass123'),
('Ulysses Carter', 'ulysses21@fisa.com', 'pass123'),
('Victoria Perez', 'victoria22@fisa.com', 'pass123'),
('William Roberts', 'william23@fisa.com', 'pass123'),
('Xavier Turner', 'xavier24@fisa.com', 'pass123'),
('Yvonne Phillips', 'yvonne25@fisa.com', 'pass123'),
('Zachary Campbell', 'zach26@fisa.com', 'pass123'),
('Amy Rogers', 'amy27@fisa.com', 'pass123'),
('Brian Stewart', 'brian28@fisa.com', 'pass123'),
('Chloe Edwards', 'chloe29@fisa.com', 'pass123'),
('Daniel Flores', 'daniel30@fisa.com', 'pass123');

-- ==========================
-- accommodation (30개 생성)
-- accom_category : 'ROOM' or 'ENTIRE'
-- building_category : 'HOUSE', 'APARTMENT', 'GUESTHOUSE', 'HOTEL'
-- amenities : 0b 비트마스크 (와이파이+에어컨 = 0b1100 = 12)
-- ==========================
INSERT INTO accommodation (accom_category, bedrooms, beds, bathrooms, price, building_category, amenities) VALUES
('ROOM', 1, 1, 1, 50, 'HOUSE', 0b0100), -- 와이파이
('ENTIRE', 2, 2, 1, 120, 'APARTMENT', 0b1100), -- 와이파이+에어컨
('ROOM', 1, 1, 1, 70, 'GUESTHOUSE', 0b10000), -- tv
('ENTIRE', 3, 3, 2, 200, 'HOUSE', 0b10100), -- 와이파이+tv
('ROOM', 1, 2, 1, 80, 'HOTEL', 0b1000000), -- 무료 주차
('ENTIRE', 4, 4, 2, 250, 'APARTMENT', 0b111100), -- 와이파이+에어컨+tv+해변
('ROOM', 1, 1, 1, 60, 'HOUSE', 0b10), -- 수영장
('ENTIRE', 2, 2, 1, 150, 'GUESTHOUSE', 0b1000100), -- 욕조+와이파이
('ROOM', 1, 1, 1, 55, 'APARTMENT', 0b1000), -- 에어컨
('ENTIRE', 5, 6, 3, 400, 'HOUSE', 0b1111111), -- 모든 편의시설
('ROOM', 1, 1, 1, 65, 'HOTEL', 0b10000), -- tv
('ENTIRE', 2, 2, 2, 180, 'APARTMENT', 0b110), -- 수영장+와이파이
('ROOM', 1, 1, 1, 40, 'GUESTHOUSE', 0b0), -- 없음
('ENTIRE', 3, 3, 2, 220, 'HOUSE', 0b101010), -- 수영장+에어컨+해변
('ROOM', 1, 1, 1, 55, 'APARTMENT', 0b100), -- 와이파이
('ENTIRE', 4, 5, 2, 300, 'HOTEL', 0b111000), -- tv+해변+주차
('ROOM', 1, 1, 1, 45, 'HOUSE', 0b1), -- 욕조
('ENTIRE', 2, 2, 1, 160, 'APARTMENT', 0b100010), -- 수영장+에어컨
('ROOM', 1, 1, 1, 50, 'HOTEL', 0b100000), -- 해변
('ENTIRE', 6, 8, 3, 500, 'HOUSE', 0b111111), -- 욕조 제외
('ROOM', 1, 1, 1, 60, 'GUESTHOUSE', 0b100100), -- 욕조+에어컨
('ENTIRE', 3, 3, 2, 240, 'APARTMENT', 0b101000), -- 에어컨+주차
('ROOM', 1, 1, 1, 70, 'HOUSE', 0b10), -- 수영장
('ENTIRE', 4, 5, 2, 280, 'HOTEL', 0b1001000), -- 욕조+주차
('ROOM', 1, 1, 1, 65, 'APARTMENT', 0b11100), -- 와이파이+에어컨+tv
('ENTIRE', 2, 3, 1, 140, 'HOUSE', 0b1100), -- 와이파이+에어컨
('ROOM', 1, 1, 1, 50, 'GUESTHOUSE', 0b1000000), -- 주차
('ENTIRE', 3, 4, 2, 210, 'APARTMENT', 0b1111111), -- full
('ROOM', 1, 1, 1, 55, 'HOUSE', 0b10000), -- tv
('ENTIRE', 5, 6, 3, 420, 'HOTEL', 0b111010); -- 일부 조합

-- ==========================
-- team_users (팀과 사용자 매핑, 30명 분배)
-- ==========================
INSERT INTO team_users (team_id, user_id) VALUES
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),
(2,7),(2,8),(2,9),(2,10),(2,11),(2,12),
(3,13),(3,14),(3,15),(3,16),(3,17),(3,18),
(4,19),(4,20),(4,21),(4,22),(4,23),(4,24),
(5,25),(5,26),(5,27),(5,28),(5,29),(5,30);

-- ==========================
-- team_accom (팀과 숙소 매핑)
-- like : 단순 카운트
-- ==========================
INSERT INTO team_accom (accom_id, team_id, `like`) VALUES
(1,1,3),(2,1,5),(3,2,2),(4,2,4),
(5,3,1),(6,3,6),(7,4,3),(8,4,5),
(9,5,2),(10,5,7),(11,1,4),(12,2,3),
(13,3,1),(14,4,5),(15,5,2),(16,1,4),
(17,2,3),(18,3,5),(19,4,2),(20,5,6),
(21,1,3),(22,2,5),(23,3,2),(24,4,6),
(25,5,4),(26,1,2),(27,2,3),(28,3,5),
(29,4,1),(30,5,7);