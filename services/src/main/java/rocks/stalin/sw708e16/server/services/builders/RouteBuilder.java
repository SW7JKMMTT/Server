package rocks.stalin.sw708e16.server.services.builders;

import org.bson.types.ObjectId;
import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;
import rocks.stalin.sw708e16.server.persistence.DriverDao;
import rocks.stalin.sw708e16.server.persistence.VehicleDao;

import java.util.ArrayList;

public class RouteBuilder {
    private ObjectId vehicleid;

    public RouteBuilder() {
    }

    public void setVehicleid(ObjectId vehicleid) {
        this.vehicleid = vehicleid;
    }

    public ObjectId getVehicleid() {
        return vehicleid;
    }

    public Route build(VehicleDao vehicleDao, Driver driver) {
        Vehicle vehicle = vehicleDao.byId(this.vehicleid);
        return new Route(new ArrayList<Waypoint>(), driver, vehicle);
    }
}
