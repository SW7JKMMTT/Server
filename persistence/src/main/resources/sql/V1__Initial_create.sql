CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;

--Users
CREATE TABLE usericon
(
    id BIGINT PRIMARY KEY NOT NULL,
    filepath VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX UK__UserIcon__FilePath ON usericon (filepath);

CREATE TABLE myuser
(
    id BIGINT PRIMARY KEY NOT NULL,
    givenname VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    icon_id BIGINT,
    CONSTRAINT FK__MyUser__UserIcon FOREIGN KEY (icon_id) REFERENCES usericon (id)
);
CREATE UNIQUE INDEX UK__MyUser__Username ON myuser (username);

CREATE TABLE driver
(
    id BIGINT PRIMARY KEY NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT FK__Driver__MyUser FOREIGN KEY (user_id) REFERENCES myuser (id)
);
CREATE UNIQUE INDEX UK__Driver__User_Id ON driver (user_id);

--Authorization
CREATE TABLE authtoken
(
    id BIGINT PRIMARY KEY NOT NULL,
    created DATE,
    expires DATE,
    token VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT FK__AuthToken__MyUser FOREIGN KEY (user_id) REFERENCES myuser (id)
);
CREATE UNIQUE INDEX UK__AuthToken__token ON authtoken (token);

--Permissions
CREATE TABLE permission
(
    id BIGINT PRIMARY KEY NOT NULL,
    permission VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT FK__Permission__MyUser FOREIGN KEY (user_id) REFERENCES myuser (id)
);

CREATE TABLE vehicleicon
(
    id BIGINT PRIMARY KEY NOT NULL,
    filepath VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX UK__Vehicle__FilePath ON vehicleicon (filepath);

CREATE TABLE vehicle
(
    id BIGINT PRIMARY KEY NOT NULL,
    make VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    vin VARCHAR(32) NOT NULL,
    vintage INTEGER NOT NULL,
    vehicleicon_id BIGINT,
    CONSTRAINT FK_Vehicle__VehicleIcon FOREIGN KEY (vehicleicon_id) REFERENCES vehicleicon (id)
);
CREATE UNIQUE INDEX UK__Vehicle__Vin ON vehicle (vin);

CREATE TABLE route
(
    id BIGINT PRIMARY KEY NOT NULL,
    routestate VARCHAR(255) NOT NULL,
    driver_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    CONSTRAINT FK__Route__Driver FOREIGN KEY (driver_id) REFERENCES driver (id),
    CONSTRAINT FK__Router__vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle (id)
);

CREATE TABLE waypoint
(
    id BIGINT PRIMARY KEY NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    route_id BIGINT NOT NULL,
    CONSTRAINT FK__Waypoint__Route FOREIGN KEY (route_id) REFERENCES route (id)
);
