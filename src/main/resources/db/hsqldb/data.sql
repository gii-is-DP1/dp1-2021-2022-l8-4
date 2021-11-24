INSERT INTO achievements(id,name,reward_points,condition) VALUES (1,'Mi primera partida',10,TRUE);

INSERT INTO cards(id,cost,name,type) VALUES(1,5,'Monstruo Alfa',1);
INSERT INTO cards(id,cost,name,type) VALUES(2,3,'Ataque acido',1); 
INSERT INTO cards(id,cost,name,type) VALUES(3,5,'Apartamentos',1); 
INSERT INTO cards(id,cost,name,type) VALUES(4,4,'Armadura de chapa',1); 
INSERT INTO cards(id,cost,name,type) VALUES(5,4,'Habitante subterraneo',1); 
INSERT INTO cards(id,cost,name,type) VALUES(6,5,'Madriguera',1); 
INSERT INTO cards(id,cost,name,type) VALUES(7,3,'Camuflaje',1); 
INSERT INTO cards(id,cost,name,type) VALUES(8,4,'Tren de cercanias',1); 
INSERT INTO cards(id,cost,name,type) VALUES(9,3,'Destruccion total',1); 
INSERT INTO cards(id,cost,name,type) VALUES(10,3,'Bazar de la esquina',1); 

INSERT INTO boards(id,tokyo_city_status,tokyo_bay_status) VALUES (1,1,0);
INSERT INTO boards(id,tokyo_city_status,tokyo_bay_status) VALUES (2,1,0);

INSERT INTO users(id,username,email,password,enabled) VALUES (1,'user1','user1@email.com','u53r1', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (1,1,'admin');

INSERT INTO users(id,username,email,password,enabled) VALUES (2,'user2','user2@email.com','u53r2', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (2,2,'user');

INSERT INTO users(id,username,email,password,enabled) VALUES (3,'user3','user3@email.com','u53r3', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (3,3,'admin');

INSERT INTO users(id,username,email,password,enabled) VALUES (4,'fire','fire@email.com','fire', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (4,4,'admin');

INSERT INTO games(id,name,user_id,turn,winner,start_time,end_time,board_id,max_number_of_players) VALUES (1,'Partida tremenda',1,'3',null,'2020-11-01T10:00:00','2020-11-01T11:00:00',1,6);
INSERT INTO games(id,name,user_id,turn,winner,start_time,end_time,board_id,max_number_of_players) VALUES (2,'Partida tremenda ya jugada',2,'15','Rick360','2020-11-01T10:00:00','2020-11-01T11:00:00',2,6);

INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (1,1,0,2,10,0,1,1);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (2,5,10,0,10,0,1,2);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (3,3,10,0,0,1,1,3);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (4,4,0,8,2,2,1,4);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (5,3,10,0,0,1,2,1);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (6,4,0,8,2,1,2,2);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (7,2,0,8,2,2,2,3);



INSERT INTO playerStatus(id,player_id,status,amount) VALUES (1, 1, 1,1 );
INSERT INTO playerStatus(id,player_id,status,amount) VALUES (2, 1, 0,1 );
INSERT INTO playerStatus(id,player_id,status,amount) VALUES (3, 4, 0,2 );

INSERT INTO boards_cards(id,board_id,card_id,sold) VALUES (1, 1, 1,FALSE);
