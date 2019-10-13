CREATE TABLE USER
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255)
);

CREATE TABLE PROJECT
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    name             VARCHAR(255) NOT NULL,
    project_owner_id INT,
    foreign key (project_owner_id) references USER (id)
);

CREATE TABLE ISSUE
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    reporter_id INT,
    assignee_id INT,
    project_id  INT          NOT NULL,
    type        VARCHAR(255),
    status      VARCHAR(255),
    description VARCHAR(1200),
    foreign key (project_id) references PROJECT (id),
    foreign key (reporter_id) references USER (id),
    foreign key (assignee_id) references USER (id)
);
