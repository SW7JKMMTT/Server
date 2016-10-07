INSERT INTO WeekSchedule (id, name, thumbnail_id, createdOn, lastEdit, department_id)
    VALUES (1, 'marks uge', 1, TIMESTAMP '2001-09-11 13:37:00', TIMESTAMP '2001-09-11 13:37:00', 1);

INSERT INTO Weekday (id, day, weekSchedule_id, lastEdit)
    VALUES (1, 0, 1, TIMESTAMP '2001-09-11 13:37:00');

INSERT INTO Weekday__Frame (id, weekdayframe_index, weekday_id, frame_id)
    VALUES (1, 0, 1, 1);

INSERT INTO User__WeekSchedule (user_id, weekSchedule_id)
    VALUES (1, 1);

INSERT INTO WeekSchedule (id, name, thumbnail_id, createdOn, lastEdit, department_id)
    VALUES (2, 'test', 1, TIMESTAMP '2001-09-11 13:37:00', TIMESTAMP '2001-09-11 13:37:00', 1);

INSERT INTO Weekday (id, day, weekSchedule_id, lastEdit)
    VALUES (2, 0, 2, TIMESTAMP '2001-09-11 13:37:00');

INSERT INTO Weekday (id, day, weekSchedule_id, lastEdit)
    VALUES (3, 1, 2, TIMESTAMP '2001-09-11 13:37:00');

INSERT INTO Weekday__Frame (id, weekdayframe_index, weekday_id, frame_id)
    VALUES (2, 0, 2, 1);

INSERT INTO Weekday__Frame (id, weekdayframe_index, weekday_id, frame_id)
    VALUES (3, 1, 2, 1);

INSERT INTO Weekday__Frame (id, weekdayframe_index, weekday_id, frame_id)
    VALUES (4, 0, 3, 1);

INSERT INTO Weekday__Frame (id, weekdayframe_index, weekday_id, frame_id)
    VALUES (5, 1, 3, 1);

INSERT INTO User__WeekSchedule (user_id, weekSchedule_id)
    VALUES (1, 2);

INSERT INTO User__WeekSchedule (user_id, weekSchedule_id)
    VALUES (2, 2);
