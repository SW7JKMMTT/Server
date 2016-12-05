package rocks.stalin.sw708e16.server.services.builders;

import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.RouteState;
import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;
import rocks.stalin.sw708e16.server.persistence.DriverDao;
import rocks.stalin.sw708e16.server.persistence.VehicleDao;

import java.util.ArrayList;

public class RouteBuilder {
    private Long vehicleid;
    private Long driverid;
    private RouteState routeState;

    public RouteBuilder() {
    }

    public void setVehicleid(long vehicleid) {
        this.vehicleid = vehicleid;
    }

    public Long getVehicleid() {
        return vehicleid;
    }

    public Long getDriverid() {
        return driverid;
    }

    public void setDriverid(long driverid) {
        this.driverid = driverid;
    }

    public RouteState getRouteState() {
        return routeState;
    }

    public void setRouteState(RouteState routeState) {
        this.routeState = routeState;
    }

    /**
     * Builds the {@link Route} object.
     *
     * @param vehicleDao Used to find the {@link Vehicle}.
     * @param driverDao Used to find the {@link Driver}.
     * @return The {@link Route} created.
     */
    public Route build(VehicleDao vehicleDao, DriverDao driverDao) {
        Vehicle vehicle = vehicleDao.byId(this.vehicleid);
        Driver driver = driverDao.byId(this.driverid);

        if (vehicle == null)
            throw new IllegalArgumentException("Vehicle given was not found.");

        if (driver == null)
            throw new IllegalArgumentException("Driver given was not found.");

        Route route = new Route(new ArrayList<Waypoint>(), driver, vehicle);
        if (routeState != null)
            route.setRouteState(routeState);

        return route;
    }

    /**
     * Merges all info from the {@link RouteBuilder} into a {@link Route}.
     *
     * @param route The {@link Route} to merge into.
     * @return The {@link Route} after being merged.
     */
    public Route merge(VehicleDao vehicleDao, DriverDao driverDao, Route route) {
        if (route == null)
            throw new IllegalArgumentException("Route to merge was null.");

        if (this.vehicleid != null) {
            Vehicle vehicle = vehicleDao.byId(this.vehicleid);
            if (vehicle != null) {
                route.setVehicle(vehicle);
            }
        }

        if (this.driverid != null) {
            Driver driver = driverDao.byId(this.driverid);
            if (driver != null) {
                route.setDriver(driver);
            }
        }

        if (this.routeState != null) {
            route.setRouteState(this.routeState);
        }

        return route;
    }
}
