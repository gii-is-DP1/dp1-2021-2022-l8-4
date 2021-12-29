INSERT INTO achievements(id,name,description,reward_points,metric,goal) VALUES (1,'Mi primera partida','Juega tu primera partida',10,0,1);

INSERT INTO cards(id,cost,card_enum,type) VALUES(1,5,'apartmentBuilding',0);
INSERT INTO cards(id,cost,card_enum,type) VALUES(2,3,'cornerStore',0); 
INSERT INTO cards(id,cost,card_enum,type) VALUES(3,4,'commuterTrain',0); 
INSERT INTO cards(id,cost,card_enum,type) VALUES(4,8,'energize',0); 
INSERT INTO cards(id,cost,card_enum,type) VALUES(5,3,'fireBlast',0); 
INSERT INTO cards(id,cost,card_enum,type) VALUES(6,7,'evacuationOrders',0);
INSERT INTO cards(id,cost,card_enum,type) VALUES(7,7,'evacuationOrders',0); 
INSERT INTO cards(id,cost,card_enum,type) VALUES(8,3,'heal',0); 
INSERT INTO cards(id,cost,card_enum,type) VALUES(9,6,'gasRefinery',0);
INSERT INTO cards(id,cost,card_enum,type) VALUES(10,4,'highAltitudeBombing',0);
INSERT INTO cards(id,cost,card_enum,type) VALUES(11,5,'jetFighters',0);
INSERT INTO cards(id,cost,card_enum,type) VALUES(12,3,'nationalGuard',0);
INSERT INTO cards(id,cost,card_enum,type) VALUES(13,6,'acidAttack',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(14,5,'alphaMonster',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(15,4,'fireBreathing',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(16,3,'friendOfChildren',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(17,7,'extraHead',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(18,7,'extraHead',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(19,5,'giantBrain',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(20,3,'completeDestruction',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(21,4,'evenBigger',0);
INSERT INTO cards(id,cost,card_enum,type) VALUES(22,4,'gourmet',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(23,3,'camouflage',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(24,4,'armorPlating',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(25,7,'itHasAChild',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(26,3,'alienMetabolism',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(27,3,'dedicatedNewsTeam',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(28,5,'herbivore',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(29,3,'energyHoarder',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(30,7,'novaBreath',1);

INSERT INTO users(id,username,email,password,enabled) VALUES (1,'user1','user1@email.com','u53r1', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (1,1,'admin');
INSERT INTO users(id,username,email,password,enabled) VALUES (2,'user2','user2@email.com','u53r2', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (2,2,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (3,'user3','user3@email.com','u53r3', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (3,3,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (4,'Fire','fire@email.com','fire', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (4,4,'admin');

INSERT INTO users(id,username,email,password,enabled) VALUES (5,'Rosa','rosa@email.com','rosa', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (5,5,'admin');
INSERT INTO users(id,username,email,password,enabled) VALUES (6,'Noelia','noelia@email.com','noelia', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (6,6,'admin');
INSERT INTO users(id,username,email,password,enabled) VALUES (7,'Rick','rick@email.com','rick', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (7,7,'admin');
INSERT INTO users(id,username,email,password,enabled) VALUES (8,'Sara','sara@email.com','sara', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (8,8,'admin');
INSERT INTO users(id,username,email,password,enabled) VALUES (9,'Carlos','carlos@email.com','carlos', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (9,9,'admin');


INSERT INTO games(id,name,user_id,turn,winner,start_time,end_time,max_number_of_players) VALUES (1,'Partida tremenda',1,'3',null,'2020-11-01T10:00:00','2020-11-01T11:00:00',6);
INSERT INTO games(id,name,user_id,turn,winner,start_time,end_time,max_number_of_players) VALUES (2,'Partida tremenda ya jugada',2,'15','user2','2020-11-01T10:00:00','2020-11-01T11:00:00',6);

INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (1,1,0,2,10,0,1,1);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (2,5,10,0,10,0,1,2);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (3,3,10,0,20,1,1,3);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (4,4,0,8,2,2,1,4);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (5,3,10,0,0,1,2,1);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (6,4,0,8,2,1,2,2);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (7,2,0,8,2,2,2,3);

INSERT INTO playerStatus(id,player_id,status,amount) VALUES (1, 1, 1,1 );
INSERT INTO playerStatus(id,player_id,status,amount) VALUES (2, 1, 0,1 );
INSERT INTO playerStatus(id,player_id,status,amount) VALUES (3, 4, 0,2 );

INSERT INTO games_cards(id,game_id,card_id,sold) VALUES (1, 1, 1,FALSE);
INSERT INTO games_cards(id,game_id,card_id,sold) VALUES (2, 1, 2,FALSE);
INSERT INTO games_cards(id,game_id,card_id,sold) VALUES (3, 1, 3,FALSE);

