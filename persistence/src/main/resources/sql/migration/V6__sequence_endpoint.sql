ALTER TABLE Sequence
    DROP FOREIGN KEY Sequence__titlePictogram_id;

ALTER TABLE Sequence
    CHANGE titlePictogram_id thumbnail_id BIGINT NOT NULL;

ALTER TABLE Sequence
    ADD CONSTRAINT Sequence__thumbnail_id
        FOREIGN KEY (thumbnail_id) REFERENCES Pictogram (frame_id)
        ON DELETE CASCADE;

ALTER TABLE Sequence__Frame
    DROP FOREIGN KEY Sequence__Frame__choice_id;

ALTER TABLE Sequence__Frame
    ADD CONSTRAINT Sequence__Frame__sequence_id
        FOREIGN KEY (sequence_id) REFERENCES Sequence (frame_id)
        ON DELETE CASCADE;

ALTER TABLE Sequence__Frame
    DROP FOREIGN KEY Sequence__Frame__pictoFrame_id;

ALTER TABLE Sequence__Frame
    ADD CONSTRAINT Sequence__Frame__pictoFrame_id
        FOREIGN KEY (frame_id) REFERENCES Frame (id)
        ON DELETE CASCADE;
