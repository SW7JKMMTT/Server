CREATE TABLE vehicledata
(
    id BIGINT PRIMARY KEY NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    route_id BIGINT NOT NULL,
    CONSTRAINT FK__VehicleData__Route FOREIGN KEY (route_id) REFERENCES route (id)
);

CREATE TABLE vehicledatapoint
(
    id BIGINT PRIMARY KEY NOT NULL,
    datatype INTEGER NOT NULL,
    vehicledata_id BIGINT NOT NULL,
    CONSTRAINT FK__VehicleDataPoint__VehicleData FOREIGN KEY (vehicledata_id) REFERENCES vehicledata (id)
);

CREATE TABLE floatvehicledatapoint
(
    id BIGINT PRIMARY KEY NOT NULL,
    value DOUBLE PRECISION,
    CONSTRAINT FK__FloatVehicleDataPoint__VehicleDataPoint FOREIGN KEY (id) REFERENCES vehicledatapoint (id)
);
