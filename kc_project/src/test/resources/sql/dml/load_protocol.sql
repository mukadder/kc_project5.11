--delete from protocol;

--delete from protocol_document;

--commit;

insert into PROTOCOL_DOCUMENT (document_number, UPDATE_TIMESTAMP,UPDATE_USER,OBJ_ID) values (1, to_date('2010-01-28 12:00:00','YYYY-MM-DD HH24:MI:SS'),'kp','e157faf5-ba82-4619-82ce-535cc611d1f6');

insert into PROTOCOL (PROTOCOL_ID, DOCUMENT_NUMBER, PROTOCOL_NUMBER, SEQUENCE_NUMBER, PROTOCOL_STATUS_CODE, PROTOCOL_TYPE_CODE, TITLE, UPDATE_TIMESTAMP,UPDATE_USER,OBJ_ID) values (1229, 1, '001', 1, 101, 1, 'title', to_date('2010-01-28 12:00:00','YYYY-MM-DD HH24:MI:SS'),'kp','69645bf6-2845-473c-ae0b-620b0a6fb10b');

insert into PROTOCOL (PROTOCOL_ID, DOCUMENT_NUMBER, PROTOCOL_NUMBER, SEQUENCE_NUMBER, PROTOCOL_STATUS_CODE, PROTOCOL_TYPE_CODE, TITLE, UPDATE_TIMESTAMP,UPDATE_USER,OBJ_ID) values (1230, 1, '001', 2, 102, 1, 'title', to_date('2010-01-28 12:00:00','YYYY-MM-DD HH24:MI:SS'),'kp','74b661af-f3b0-4b84-b2fe-ea6e8ada6b3f');

insert into PROTOCOL (PROTOCOL_ID, DOCUMENT_NUMBER, PROTOCOL_NUMBER, SEQUENCE_NUMBER, PROTOCOL_STATUS_CODE, PROTOCOL_TYPE_CODE, TITLE, UPDATE_TIMESTAMP,UPDATE_USER,OBJ_ID) values (1231, 1, '0012', 3, 104, 1, 'title', to_date('2010-01-28 12:00:00','YYYY-MM-DD HH24:MI:SS'),'kp','5874c7cf-8877-41d3-9de8-de5fdff4d423');

insert into PROTOCOL (PROTOCOL_ID, DOCUMENT_NUMBER, PROTOCOL_NUMBER, SEQUENCE_NUMBER, PROTOCOL_STATUS_CODE, PROTOCOL_TYPE_CODE, TITLE, UPDATE_TIMESTAMP,UPDATE_USER,OBJ_ID) values (1232, 1, '0012R', 3, 105, 1, 'title', to_date('2010-01-28 12:00:00','YYYY-MM-DD HH24:MI:SS'),'kp','2f228e08-10dd-4dff-83d1-e1f168410c31');
insert into PROTOCOL (PROTOCOL_ID, DOCUMENT_NUMBER, PROTOCOL_NUMBER, SEQUENCE_NUMBER, PROTOCOL_STATUS_CODE, PROTOCOL_TYPE_CODE, TITLE, UPDATE_TIMESTAMP,UPDATE_USER,OBJ_ID) values (1233, 1, '001', 3, 200, 1, 'title', to_date('2010-01-28 12:00:00','YYYY-MM-DD HH24:MI:SS'),'kp','30b35543-23ec-460d-b42d-836680c2d778');



insert into PROTOCOL (PROTOCOL_ID, DOCUMENT_NUMBER, PROTOCOL_NUMBER, SEQUENCE_NUMBER, PROTOCOL_STATUS_CODE, PROTOCOL_TYPE_CODE, TITLE, UPDATE_TIMESTAMP,UPDATE_USER,OBJ_ID) values (1234, 1, '002', 1, 102, 1, 'title', to_date('2010-01-28 12:00:00','YYYY-MM-DD HH24:MI:SS'),'kp','fa82d1b3-da22-4947-96fc-36c3030121a6');

insert into PROTOCOL (PROTOCOL_ID, DOCUMENT_NUMBER, PROTOCOL_NUMBER, SEQUENCE_NUMBER, PROTOCOL_STATUS_CODE, PROTOCOL_TYPE_CODE, TITLE, UPDATE_TIMESTAMP,UPDATE_USER,OBJ_ID) values (1235, 1, '0022R', 3, 200, 1, 'title', to_date('2010-01-28 12:00:00','YYYY-MM-DD HH24:MI:SS'),'kp','6bcac631-a740-462f-8791-afcf91d31406');
insert into PROTOCOL (PROTOCOL_ID, DOCUMENT_NUMBER, PROTOCOL_NUMBER, SEQUENCE_NUMBER, PROTOCOL_STATUS_CODE, PROTOCOL_TYPE_CODE, TITLE, UPDATE_TIMESTAMP,UPDATE_USER,OBJ_ID) values (1236, 1, '002', 2, 200, 1, 'title', to_date('2010-01-28 12:00:00','YYYY-MM-DD HH24:MI:SS'),'kp','5e23953e-a778-438d-8883-4b00151892b1');
