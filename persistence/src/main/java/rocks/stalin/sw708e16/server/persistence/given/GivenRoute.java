package rocks.stalin.sw708e16.server.persistence.given;

import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;
import rocks.stalin.sw708e16.server.persistence.RouteDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GivenRoute {
    private List<Waypoint> waypoints = new ArrayList<Waypoint>();
    private Driver driver;
    private Vehicle vehicle;

    public GivenRoute withDriver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public GivenRoute withWaypoint(Waypoint waypoint) {
        waypoints.add(waypoints.size(), waypoint);
        return this;
    }

    public GivenRoute withWaypoints(Collection<Waypoint> waypoints) {
        waypoints.addAll(waypoints);
        return this;
    }

    public GivenRoute withVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public Route in(RouteDao routeDao) {
        Route route = new Route(waypoints, driver, vehicle);
        routeDao.add(route);
        return route;
    }
}
