INSERT INTO USER (ID, username, password, department_id) VALUES (5, 'Marius', 'hunter2', 2);
INSERT INTO PERMISSION (permission, user_id) VALUES ('Guardian', 5);
INSERT INTO PERMISSION (permission, user_id) VALUES ('User', 5);
INSERT INTO AUTHTOKEN (TOKEN, USER_ID) VALUES('FAKEMARIUSTOKEN', 5);
