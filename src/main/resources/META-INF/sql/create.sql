CREATE TABLE USER
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
);

CREATE TABLE USER_CREDENTIALS
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    user_id       INT NOT NULL,
    password_hash VARCHAR(255),
    changed_date  DATETIME,
    foreign key (user_id) references USER (id)
);

CREATE TABLE PROJECT
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    name             VARCHAR(255) NOT NULL,
    project_owner_id INT          NOT NULL,
    foreign key (project_owner_id) references USER (id)
);

CREATE TABLE ISSUE
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    reporter_id INT          NOT NULL,
    assignee_id INT,
    project_id  INT          NOT NULL,
    type        VARCHAR(255),
    status      VARCHAR(255),
    description VARCHAR(1200),
    foreign key (project_id) references PROJECT (id),
    foreign key (reporter_id) references USER (id),
    foreign key (assignee_id) references USER (id)
);

CREATE TABLE ROLE
(
    id                  INT PRIMARY KEY AUTO_INCREMENT,
    user_credentials_id INT          NOT NULL,
    role_name           VARCHAR(255) NOT NULL,
    foreign key (user_credentials_id) references USER_CREDENTIALS (id)
);
