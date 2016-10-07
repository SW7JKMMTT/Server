CREATE TABLE UserIcon
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    filePath VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX UK_lqwoxuhoukdn2scj0m7kcb4ro ON UserIcon (filePath);

CREATE TABLE User
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    icon_id BIGINT,
    CONSTRAINT FKh34emwbxe05wo9jxjcqw50i6w FOREIGN KEY (icon_id) REFERENCES UserIcon (id)
);
CREATE INDEX FKh34emwbxe05wo9jxjcqw50i6w ON User (icon_id);
CREATE UNIQUE INDEX UK_jreodf78a7pl5qidfh43axdfb ON User (username);

CREATE TABLE Perms
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    permission VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT FKtjdve5hmpxwok9u0154mcdqnp FOREIGN KEY (user_id) REFERENCES User (id)
);
CREATE INDEX FKtjdve5hmpxwok9u0154mcdqnp ON Perms (user_id);

CREATE TABLE AuthToken
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    created DATE,
    expires DATE,
    token VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT FKtcjtkw8u9o4ew57tlcqi3oxwy FOREIGN KEY (user_id) REFERENCES User (id)
);
CREATE INDEX FKtcjtkw8u9o4ew57tlcqi3oxwy ON AuthToken (user_id);
CREATE UNIQUE INDEX UK_78fr0su32u5jt7gy0150f0gip ON AuthToken (token);
