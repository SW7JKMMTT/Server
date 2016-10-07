CREATE TABLE PictogramImage
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    filepath VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX UK_PictogramImageFilePath ON PictogramImage (filePath);

CREATE TABLE Pictogram
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    lasteditat TIMESTAMP NOT NULL,
    accesslevel INT NOT NULL,
    owner_id BIGINT,
    pictogramimage_id BIGINT,
    department_id BIGINT,
    CONSTRAINT Pictogram__id_PictogramImage FOREIGN KEY (pictogramimage_id) REFERENCES PictogramImage (id),
    CONSTRAINT Pictogram__id_department FOREIGN KEY (department_id) REFERENCES Department (id),
    CONSTRAINT Pictogram__id_owner FOREIGN KEY (owner_id) REFERENCES User (id)
);
