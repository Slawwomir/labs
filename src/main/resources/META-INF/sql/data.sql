-- USERS
INSERT INTO USER(id, name)
VALUES (1, 'Tytus'); -- Password = dupa

INSERT INTO USER(id, name)
VALUES (2, 'Romek');

INSERT INTO USER(id, name)
VALUES (3, 'A`Tomek');

-- USER_CREDENTIALS

INSERT INTO USER_CREDENTIALS(id, user_id, password_hash)
VALUES (1, 1, '60a5d3e4100fe8afa5ee0103739a45711d50d7f3ba7280d8a95b51f5d04aa4b8');

INSERT INTO USER_CREDENTIALS(id, user_id, password_hash)
VALUES (2, 2, '60a5d3e4100fe8afa5ee0103739a45711d50d7f3ba7280d8a95b51f5d04aa4b8');

INSERT INTO USER_CREDENTIALS(id, user_id, password_hash)
VALUES (3, 3, '60a5d3e4100fe8afa5ee0103739a45711d50d7f3ba7280d8a95b51f5d04aa4b8');

-- ROLE
INSERT INTO ROLE(user_credentials_id, role_name)
VALUES (1, 'ADMIN');

INSERT INTO ROLE(user_credentials_id, role_name)
VALUES (2, 'USER');

INSERT INTO ROLE(user_credentials_id, role_name)
VALUES (3, 'USER');

-- PROJECTS
INSERT INTO PROJECT(id, name, project_owner_id)
VALUES (1, 'Praca inżynierska', 1);

INSERT INTO PROJECT(id, name, project_owner_id)
VALUES (2, 'Jakiś inny projekt', 1);

-- ISSUES
INSERT INTO ISSUE(name, reporter_id, project_id, type, status, description)
VALUES ('Popraw stronę startową', 1, 1, 'BUG', 'OPEN', 'To jest takie łatwe zadanie, po prostu to zrób');

INSERT INTO ISSUE(name, reporter_id, project_id, type, status, description)
VALUES ('Jakieś inne issue', 1, 2, 'TASK', 'OPEN', 'To jest trudne, po prostu tego nie rób');

-- PERMISSIONS
INSERT INTO PERMISSION(role_name, method_name, permission_level)
VALUES ('ADMIN', 'addPermission', 'GRANTED');

INSERT INTO PERMISSION(role_name, method_name, permission_level)
VALUES ('ADMIN', 'getPermissions', 'GRANTED');

INSERT INTO PERMISSION(role_name, method_name, permission_level)
VALUES ('ADMIN', 'removePermission', 'GRANTED');

INSERT INTO PERMISSION(role_name, method_name, permission_level)
VALUES ('ADMIN', 'getProject', 'GRANTED');

INSERT INTO PERMISSION(role_name, method_name, permission_level)
VALUES ('ADMIN', 'getIssue', 'GRANTED');

INSERT INTO PERMISSION(role_name, method_name, permission_level)
VALUES ('ADMIN', 'updateIssue', 'GRANTED');

INSERT INTO PERMISSION(role_name, method_name, permission_level)
VALUES ('ADMIN', 'updateProject', 'GRANTED');

INSERT INTO PERMISSION(role_name, method_name, permission_level)
VALUES ('ADMIN', 'addIssue', 'GRANTED');
