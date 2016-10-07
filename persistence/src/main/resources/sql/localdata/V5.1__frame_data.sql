INSERT INTO Frame (id, lastEditAt) VALUES (3, TIMESTAMP '2001-09-11 13:37:00');
INSERT INTO PictoFrame (frame_id, title, accesslevel, department_id, owner_id) VALUES (3, 'meme', 0, 2, 1337);
INSERT INTO Pictogram (frame_id, PictogramImage_id) VALUES (3, 1);

INSERT INTO Frame (id, lastEditAt) VALUES (6, TIMESTAMP '2001-09-11 13:37:00');
INSERT INTO PictoFrame (frame_id, title, accesslevel, department_id, owner_id) VALUES (6, 'pepe', 0, 2, 1337);
INSERT INTO Pictogram (frame_id, PictogramImage_id) VALUES (6, 1);

INSERT INTO Frame (id, lastEditAt) VALUES (4, TIMESTAMP '2001-09-11 13:37:00');
INSERT INTO PictoFrame (frame_id, title, accesslevel, department_id, owner_id) VALUES (4, 'dank', 0, 2, 1337);
INSERT INTO Sequence (frame_id, titlePictogram_id) VALUES (4, 2);


INSERT INTO Sequence__Frame (placement, sequence_id, frame_id) VALUES (1, 4, 3);
INSERT INTO Sequence__Frame (placement, sequence_id, frame_id) VALUES (0, 4, 1);
INSERT INTO Sequence__Frame (placement, sequence_id, frame_id) VALUES (2, 4, 6);


INSERT INTO Frame (id, lastEditAt) VALUES (5, TIMESTAMP '2001-09-11 13:37:00');
INSERT INTO Choice (frame_id) VALUES (5);

INSERT INTO Choice__PictoFrame (placement, choice_id, pictoFrame_id) VALUES (0, 5, 3);
INSERT INTO Choice__PictoFrame (placement, choice_id, pictoFrame_id) VALUES (1, 5, 1);
