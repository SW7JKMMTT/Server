/* New stuff */
CREATE TABLE Frame
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    lastEditAt TIMESTAMP NOT NULL
);

CREATE TABLE Choice
(
    frame_id BIGINT PRIMARY KEY
);

CREATE TABLE Choice__PictoFrame
(
    placement INT NOT NULL,
    choice_id BIGINT NOT NULL,
    pictoFrame_id BIGINT NOT NULL,
    PRIMARY KEY (choice_id, pictoFrame_id)
);

CREATE TABLE PictoFrame
(
    frame_id BIGINT NOT NULL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    accesslevel INT NOT NULL,
    department_id BIGINT,
    owner_id BIGINT
);

CREATE TABLE Sequence
(
    frame_id BIGINT NOT NULL PRIMARY KEY,
    titlePictogram_id BIGINT NOT NULL
);

CREATE TABLE Sequence__Frame
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    placement INT NOT NULL,
    sequence_id BIGINT NOT NULL,
    frame_id BIGINT NOT NULL
);

INSERT INTO Frame (id, lastEditAt)
    SELECT id, lastEditAt
        FROM Pictogram;

INSERT INTO PictoFrame (frame_id, title, accesslevel, department_id, owner_id)
    SELECT id, title, accesslevel, department_id, owner_id
        FROM Pictogram;

ALTER TABLE Pictogram
    DROP FOREIGN KEY Pictogram__id_department;

ALTER TABLE Pictogram
    DROP FOREIGN KEY Pictogram__id_owner;

ALTER TABLE Pictogram
    CHANGE id frame_id BIGINT;

ALTER TABLE Pictogram
    DROP COLUMN title;

ALTER TABLE Pictogram
    DROP COLUMN accesslevel;

ALTER TABLE Pictogram
    DROP COLUMN department_id;

ALTER TABLE Pictogram
    DROP COLUMN owner_ID;

ALTER TABLE Pictogram
    DROP COLUMN lastEditAt;

/* Foreign key galore */
ALTER TABLE Pictogram
    ADD CONSTRAINT Pictogram__frame_id
        FOREIGN KEY (frame_id) REFERENCES Frame (id);

ALTER TABLE Choice
    ADD CONSTRAINT Choice__frame_id
        FOREIGN KEY (frame_id) REFERENCES Frame (id);

ALTER TABLE Choice__PictoFrame
    ADD CONSTRAINT Choice__PictoFrame__choice_id
        FOREIGN KEY (choice_id) REFERENCES Choice (frame_id);

ALTER TABLE Choice__PictoFrame
    ADD CONSTRAINT Choice__PictoFrame__pictoFrame_id
        FOREIGN KEY (pictoFrame_id) REFERENCES PictoFrame (frame_id);

ALTER TABLE PictoFrame
    ADD CONSTRAINT PictoFrame__frame_id
        FOREIGN KEY (frame_id) REFERENCES Frame (id);

ALTER TABLE PictoFrame
    ADD CONSTRAINT PictoFrame__department_id
        FOREIGN KEY (department_id) REFERENCES Department (id);

ALTER TABLE PictoFrame
    ADD CONSTRAINT PictoFrame__owner_id
        FOREIGN KEY (owner_id) REFERENCES User (id);

ALTER TABLE Sequence
    ADD CONSTRAINT Sequence__frame_id
        FOREIGN KEY (frame_id) REFERENCES Frame (id);

ALTER TABLE Sequence
    ADD CONSTRAINT Sequence__titlePictogram_id
        FOREIGN KEY (titlePictogram_id) REFERENCES Pictogram (frame_id);

ALTER TABLE Sequence__Frame
    ADD CONSTRAINT Sequence__Frame__choice_id
        FOREIGN KEY (sequence_id) REFERENCES Sequence (frame_id);

ALTER TABLE Sequence__Frame
    ADD CONSTRAINT Sequence__Frame__pictoFrame_id
        FOREIGN KEY (frame_id) REFERENCES Frame (id);
