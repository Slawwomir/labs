-- USERS
INSERT INTO USER(id, name)
VALUES (1, 'Tomek');

-- PROJECTS
INSERT INTO PROJECT(name, project_owner_id)
VALUES ('Praca inżynierska', 1);

INSERT INTO PROJECT(name, project_owner_id)
VALUES ('Jakiś inny projekt', 1);

-- ISSUES
INSERT INTO ISSUE(name, reporter_id, project_id, type, status, description)
VALUES ('Popraw stronę startową', 1, 1, 'BUG', 'OPEN', 'To jest takie łatwe zadanie, po prostu to zrób');

INSERT INTO ISSUE(name, reporter_id, project_id, type, status, description)
VALUES ('Jakieś inne issue', 1, 2, 'TASK', 'OPEN', 'To jest trudne, po prostu tego nie rób');
