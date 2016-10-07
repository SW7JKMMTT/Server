CREATE TABLE Department
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);
CREATE UNIQUE INDEX Department_name_uindex ON Department (name);

ALTER TABLE User ADD department_id BIGINT NOT NULL;

/* Insert a department (named LEGACY) if there are any users without a department */
INSERT INTO Department(name)
    SELECT 'LEGACY'
        FROM dual
        WHERE EXISTS (
            SELECT *
                FROM User
                WHERE User.department_id = 0
        );

/* Update all of the unassociated users to the new department */
UPDATE User
    SET User.department_id = (
        SELECT id
            FROM Department
            WHERE Department.name = 'LEGACY'
    )
    WHERE User.department_id = 0;

/* Add the FK constraints */
ALTER TABLE User
    ADD CONSTRAINT User_department_id_fk
    FOREIGN KEY (department_id) REFERENCES Department (id);
ALTER TABLE User ADD CONSTRAINT User__id_Department UNIQUE (id, department_id)
