--!/bin/psql
-- Brothers, moderators
-- vim: ts=2 ws=2

DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Person CASCADE;
DROP TABLE IF EXISTS Groups CASCADE;
DROP TABLE IF EXISTS Posts CASCADE;
DROP TABLE IF EXISTS Comments CASCADE;
DROP TABLE IF EXISTS Message CASCADE;
DROP TABLE IF EXISTS Friends CASCADE;


CREATE TABLE Users (-- Поскольку у группы и пользователя почти одинакове возможности
  user_id SERIAL,
  PRIMARY KEY (user_id)
);

CREATE TABLE Person (
  user_id  INT           NOT NULL,
  name     VARCHAR(30)   NOT NULL,
  birthday DATE          NOT NULL,
  phone    INT           NOT NULL,
  mail     VARCHAR(30)   NOT NULL,
  level    INT DEFAULT 0 NOT NULL,
  mother   INT,
  father   INT,
  partner  INT,
  FOREIGN KEY (mother) REFERENCES Person (user_id),
  FOREIGN KEY (father) REFERENCES Person (user_id),
  FOREIGN KEY (partner) REFERENCES Person (user_id),
  FOREIGN KEY (user_id) REFERENCES Users (user_id),
  PRIMARY KEY (user_id)
);

CREATE TABLE Brothers (
  user_id INT NOT NULL,
  brother_id INT NOT NULL,
  PRIMARY KEY (user_id, brother_id),
  PRIMARY KEY (user_id),
  FOREIGN KEY (user_id) REFERENCES Person (user_id),
  FOREIGN KEY (brother_id) REFERENCES Person (user_id)
)

CREATE TABLE Moders (
  user_id INT NOT NULL,
  group_id INT NOT NULL,
  level INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES Person (user_id),
  FOREIGN KEY (group_id) REFERENCES Groups (group_id),
  PRIMARY KEY (user_id, group_id)
)

CREATE TABLE Friends (
  user_id   INT NOT NULL,
  friend_id INT NOT NULL,
  PRIMARY KEY (user_id, friend_id),
  FOREIGN KEY (user_id) REFERENCES Person (user_id),
  FOREIGN KEY (friend_id) REFERENCES Person (user_id)
);

CREATE TABLE Groups (
  group_id INT NOT NULL,
  owner    INT NOT NULL,
  FOREIGN KEY (owner) REFERENCES Person (user_id),
  FOREIGN KEY (group_id) REFERENCES Users (user_id),
  PRIMARY KEY (group_id)
);

CREATE TABLE Posts (
  post_id SERIAL,
  likes   INT DEFAULT 0,
  text    VARCHAR NOT NULL,
  sender  INT     NOT NULL,
  reciver INT     NOT NULL,
  FOREIGN KEY (sender) REFERENCES Users (user_id),
  FOREIGN KEY (reciver) REFERENCES Users (user_id),
  PRIMARY KEY (post_id)
);

CREATE TABLE Comments (
  comment_id SERIAL,
  likes      INT DEFAULT 0,
  text       VARCHAR   NOT NULL,
  sender     INT       NOT NULL,
  receiver   INT       NOT NULL,
  time       TIMESTAMP NOT NULL,
  FOREIGN KEY (receiver) REFERENCES Posts (post_id),
  FOREIGN KEY (sender) REFERENCES Users (user_id),
  PRIMARY KEY (comment_id)
);

CREATE TABLE Message (
  message_id SERIAL,
  text       VARCHAR   NOT NULL,
  sender     INT       NOT NULL,
  receiver   INT       NOT NULL,
  time       TIMESTAMP NOT NULL,
  FOREIGN KEY (receiver) REFERENCES Person (user_id),
  FOREIGN KEY (sender) REFERENCES Person (user_id),
  PRIMARY KEY (message_id)
);

