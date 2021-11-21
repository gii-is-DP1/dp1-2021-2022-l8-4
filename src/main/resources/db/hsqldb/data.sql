INSERT INTO achievements(id,name,reward_points,condition) VALUES (1,'Mi primera partida',10,TRUE);

INSERT INTO cards(id,cost,name,type) VALUES(1,5,'Monstruo Alfa',1); 

INSERT INTO boards(id,tokyo_city_status,tokyo_bay_status) VALUES (1,1,0);

INSERT INTO games(id,name,creator,turn,winner,start_time,end_time,board_id) VALUES (1,'Partida tremenda','Rick360','3','Rick360','2020-11-01T10:00:00','2020-11-01T11:00:00',1);

INSERT INTO users(id,username,email,password,enabled) VALUES (1,'user1','user1@email.com','u53r1', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (1,1,'admin');

INSERT INTO users(id,username,email,password,enabled) VALUES (2,'user2','user2@email.com','u53r2', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (2,2,'user');

INSERT INTO users(id,username,email,password,enabled) VALUES (3,'user3','user3@email.com','u53r3', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (3,3,'admin');

INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id) VALUES (1,1,10,2,3,0,1);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id) VALUES (2,2,10,0,0,0,1);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id) VALUES (3,3,10,0,0,1,1);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id) VALUES (4,4,4,8,2,2,1);

INSERT INTO playerStatus(id,player_id,status,amount) VALUES (1, 1, 1,1 );
INSERT INTO playerStatus(id,player_id,status,amount) VALUES (2, 1, 0,1 );
INSERT INTO playerStatus(id,player_id,status,amount) VALUES (3, 4, 0,2 );

INSERT INTO boards_cards(id,board_id,card_id,sold) VALUES (1, 1, 1,FALSE);
