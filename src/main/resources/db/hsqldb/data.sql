INSERT INTO achievements(id,name,description,reward_points,metric,goal) VALUES (1,'Mi primera partida','Juega tu primera partida',10,0,1);
INSERT INTO achievements(id,name,description,reward_points,metric,goal) VALUES (2,'Mi quinta partida','Juega tu quinta partida',15,0,5);
INSERT INTO achievements(id,name,description,reward_points,metric,goal) VALUES (3,'Mi veinteava partida','Juega tu veinteava partida',15,0,20);
INSERT INTO achievements(id,name,description,reward_points,metric,goal) VALUES (4,'Mi cincuentava partida','Juega tu cincuentava partida',20,0,50);
INSERT INTO achievements(id,name,description,reward_points,metric,goal) VALUES (5,'Mi primera victoria','Gana tu primera partida',10,1,1);
INSERT INTO achievements(id,name,description,reward_points,metric,goal) VALUES (6,'Mi quinta victoria','Gana tu quinta partida',15,1,5);
INSERT INTO achievements(id,name,description,reward_points,metric,goal) VALUES (7,'Mi veinteava victoria','Gana tu veinteava partida',15,1,20);
INSERT INTO achievements(id,name,description,reward_points,metric,goal) VALUES (8,'Mi cincuentava victoria','Gana tu cincuentava partida',20,1,50);
INSERT INTO achievements(id,name,description,reward_points,metric,goal) VALUES (9,'Mi primera carta usada','Gana tu primera partida',5,2,1);
INSERT INTO achievements(id,name,description,reward_points,metric,goal) VALUES (10,'Mi quinta carta usada','Gana tu quinta partida',10,2,5);
INSERT INTO achievements(id,name,description,reward_points,metric,goal) VALUES (11,'Mi decima carta usada','Gana tu veinteava partida',10,2,10);
INSERT INTO achievements(id,name,description,reward_points,metric,goal) VALUES (12,'Mi veinteava carta usada','Gana tu cincuentava partida',20,2,20);

INSERT INTO cards(id,cost,card_enum,type) VALUES(1,5,'APARTMENTBUILDING',0);
INSERT INTO cards(id,cost,card_enum,type) VALUES(2,3,'CORNERSTORE',0); 
INSERT INTO cards(id,cost,card_enum,type) VALUES(3,4,'COMMUTERTRAIN',0); 
INSERT INTO cards(id,cost,card_enum,type) VALUES(4,8,'ENERGIZE',0); 
INSERT INTO cards(id,cost,card_enum,type) VALUES(5,3,'FIREBLAST',0); 
INSERT INTO cards(id,cost,card_enum,type) VALUES(6,7,'EVACUATIONORDERS',0);
INSERT INTO cards(id,cost,card_enum,type) VALUES(7,7,'EVACUATIONORDERS',0); 
INSERT INTO cards(id,cost,card_enum,type) VALUES(8,3,'HEAL',0); 
INSERT INTO cards(id,cost,card_enum,type) VALUES(9,6,'GASREFINERY',0);
INSERT INTO cards(id,cost,card_enum,type) VALUES(10,4,'HIGHALTITUDEBOMBING',0);
INSERT INTO cards(id,cost,card_enum,type) VALUES(11,5,'JETFIGHTERS',0);
INSERT INTO cards(id,cost,card_enum,type) VALUES(12,3,'NATIONALGUARD',0);
INSERT INTO cards(id,cost,card_enum,type) VALUES(13,6,'ACIDATTACK',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(14,5,'ALPHAMONSTER',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(15,4,'FIREBREATHING',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(16,3,'FRIENDOFCHILDREN',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(17,7,'EXTRAHEAD',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(18,7,'EXTRAHEAD',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(19,5,'GIANTBRAIN',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(20,3,'COMPLETEDESTRUCTION',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(21,4,'EVENBIGGER',0);
INSERT INTO cards(id,cost,card_enum,type) VALUES(22,4,'GOURMET',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(23,3,'CAMOUFLAGE',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(24,4,'ARMORPLATING',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(25,7,'ITHASACHILD',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(26,3,'ALIENMETABOLISM',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(27,3,'DEDICATEDNEWSTEAM',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(28,5,'HERBIVORE',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(29,3,'ENERGYHOARDER',1);
INSERT INTO cards(id,cost,card_enum,type) VALUES(30,7,'NOVABREATH',1);

INSERT INTO users(id,username,email,password,enabled) VALUES (1,'user1','user1@email.com','$2a$10$POdm6NGv8Z.ADZAVFtchKeIXTQMAepl5JmjfLwvKQMk.ihd4.FvGC', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (1,1,'admin');
INSERT INTO users(id,username,email,password,enabled) VALUES (2,'user2','user2@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (2,2,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (3,'user3','user3@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (3,3,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (4,'Fire','fire@email.com','$2a$10$POdm6NGv8Z.ADZAVFtchKeIXTQMAepl5JmjfLwvKQMk.ihd4.FvGC', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (4,4,'admin');
INSERT INTO users(id,username,email,password,enabled) VALUES (5,'Rosa','rosa@email.com','$2a$10$POdm6NGv8Z.ADZAVFtchKeIXTQMAepl5JmjfLwvKQMk.ihd4.FvGC', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (5,5,'admin');
INSERT INTO users(id,username,email,password,enabled) VALUES (6,'Noelia','noelia@email.com','$2a$10$POdm6NGv8Z.ADZAVFtchKeIXTQMAepl5JmjfLwvKQMk.ihd4.FvGC', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (6,6,'admin');
INSERT INTO users(id,username,email,password,enabled) VALUES (7,'Rick','rick@email.com','$2a$10$POdm6NGv8Z.ADZAVFtchKeIXTQMAepl5JmjfLwvKQMk.ihd4.FvGC', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (7,7,'admin');
INSERT INTO users(id,username,email,password,enabled) VALUES (8,'Sara','sara@email.com','$2a$10$POdm6NGv8Z.ADZAVFtchKeIXTQMAepl5JmjfLwvKQMk.ihd4.FvGC', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (8,8,'admin');
INSERT INTO users(id,username,email,password,enabled) VALUES (9,'Carlos','carlos@email.com','$2a$10$POdm6NGv8Z.ADZAVFtchKeIXTQMAepl5JmjfLwvKQMk.ihd4.FvGC', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (9,9,'admin');
INSERT INTO users(id,username,email,password,enabled) VALUES (10,'master1','master1@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (10,10,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (11,'Lilia','lilia97@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (11,11,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (12,'kaipo9','kaipo9@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (12,12,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (13,'jugador1','jugador1@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (13,13,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (14,'rodri77','rodri77@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (14,14,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (15,'chapa2','chapa2@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (15,15,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (16,'manolo18','manolo18@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (16,16,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (17,'2cabezas','2cabezas@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (17,17,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (18,'Komococo','Komococo@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (18,18,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (19,'BigBilly','bigbb@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (19,19,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (20,'luciia12','luciia12@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (20,20,'user');
--Usuarios partida 2 jugadores
INSERT INTO users(id,username,email,password,enabled) VALUES (21,'Kleon','kleon@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (21,21,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (22,'Kaiipo','kaiipo@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (22,22,'user');
--Usuarios para partida sin empezar
INSERT INTO users(id,username,email,password,enabled) VALUES (23,'Joselu2','joselu2@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (23,23,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (24,'betico7','betico7@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (24,24,'user');
INSERT INTO users(id,username,email,password,enabled) VALUES (25,'solitario','solitario@email.com','$2a$10$kz.zONpoIPBYxg7znN8yr.mi45ErkcRV8pnZn.O1Rsavazm63BUgi', TRUE);
INSERT INTO authorities(id,userid,authority) VALUES (25,25,'user');

INSERT INTO games(id,name,user_id,turn,winner,start_time,end_time,max_number_of_players) VALUES (1,'Partida tremenda',1,'3',null,'2020-11-01T10:00:00','2020-11-01T11:00:00',6);
INSERT INTO games(id,name,user_id,turn,winner,start_time,end_time,max_number_of_players) VALUES (2,'Partida tremenda ya jugada',2,'15','user2','2020-11-01T10:00:00','2020-11-01T11:00:00',6);
INSERT INTO games(id,name,user_id,turn,winner,start_time,end_time,max_number_of_players) VALUES (3,'Partida Test Empezada',10,'1',null,'2020-11-10T10:30:00',null,6);
INSERT INTO games(id,name,user_id,turn,winner,start_time,end_time,max_number_of_players) VALUES (4,'Partida Test 5 jugadores',16,'8',null,'2020-11-10T10:12:54',null,6);
INSERT INTO games(id,name,user_id,turn,winner,start_time,end_time,max_number_of_players) VALUES (5,'Partida Test 2 jugadores',21,'10',null,'2021-10-10T11:09:54',null,2);
INSERT INTO games(id,name,user_id,turn,winner,start_time,end_time,max_number_of_players) VALUES (6,'Partida sin empezar',23,'0',null,null,null,4);
INSERT INTO games(id,name,user_id,turn,winner,start_time,end_time,max_number_of_players) VALUES (7,'Partida sin empezar 1 jugador',25,'0',null,null,null,6);

-- Player de game id=1, turnos ids=3,2 , estado del player 3: finalizando turno (roll amount 3) con 10 de energia
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (1,1,0,2,10,0,1,1);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (2,5,10,0,10,0,1,2);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (3,3,10,0,20,1,1,3);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (4,4,0,8,2,2,1,4);

INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (5,3,10,0,0,1,2,1);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (6,4,0,8,2,1,2,2);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (7,2,0,8,2,2,2,3);
-- Players para tests 
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (8,0,10,0,0,0,3,10);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (9,1,10,0,0,0,3,11);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (10,2,10,0,0,0,3,12);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (11,3,10,0,0,0,3,13);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (12,4,10,0,0,0,3,14);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (13,5,10,0,0,0,3,15);
-- Players para test 5 usuarios 
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (14,0,1,5,1,0,4,16);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (15,1,10,0,1,1,4,17);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (16,2,10,0,30,2,4,18);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (17,3,10,0,30,0,4,19);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (18,4,10,0,30,0,4,20);
-- Players para test con 2 usuarios
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (19,0,10,0,30,0,5,21);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (20,4,2,3,30,1,5,22);
-- Players para test partida sin empezar
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (21,1,10,0,20,0,6,23);
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (22,2,10,5,20,0,6,24);
-- Player test jugador solitario
INSERT INTO players(id,monster_name,life_points,victory_points,energy_points,location,game_id,user_id) VALUES (23,2,10,0,20,0,7,25);


INSERT INTO games_cards(id,game_id,card_id,sold) VALUES (1, 1, 1,FALSE);
INSERT INTO games_cards(id,game_id,card_id,sold) VALUES (2, 1, 2,FALSE);
INSERT INTO games_cards(id,game_id,card_id,sold) VALUES (3, 1, 3,FALSE);

