DELETE
    FROM Perms
    WHERE Perms.permission = 'ListUsers';

INSERT INTO Perms (user_id, permission)
    (
        SELECT User.id, 'User'
            FROM User
            WHERE NOT EXISTS (
                SELECT *
                    FROM Perms
                    WHERE Perms.permission = 'User'
                        AND perms.user_id = User.id
            )
    );

ALTER TABLE Perms
    RENAME TO Permission;
