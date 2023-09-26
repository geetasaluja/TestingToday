insert into player (player_id,Player_name) values (101,'test1');
insert into player (player_id,Player_name) values (102,'test2');
insert into player (player_id,Player_name) values (103,'test3');
insert into player (player_id,Player_name) values (104,'test4');

INSERT INTO  WALLET ("WALLET_ID","WALLET_BALANCE","WALLET_DATE","VERSION","PLAYER_ID") VALUES (5001,2000,now(),0,101);
INSERT INTO  WALLET ("WALLET_ID","WALLET_BALANCE","WALLET_DATE","VERSION","PLAYER_ID") VALUES (5002,3000,now(),0,102) ;
INSERT INTO  WALLET ("WALLET_ID","WALLET_BALANCE","WALLET_DATE","VERSION","PLAYER_ID") VALUES (5003,4000,now(),0,103) ;
INSERT INTO  WALLET ("WALLET_ID","WALLET_BALANCE","WALLET_DATE","VERSION","PLAYER_ID") VALUES (5004,5000,now(),0,104) ;

INSERT INTO TRANSACTION ("TRANSACTION_ID","TRANSACTION_AMOUNT","TRANSACTION_DATE","CALLER_SUPPLY_TRANS_ID","TRANSACTION_TYPE","VERSION","WALLET_ID")  VALUES(1111,100.00,now(),'testing-purpose-dummy-data','DEBIT',0,5003)
