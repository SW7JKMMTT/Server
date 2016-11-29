package rocks.stalin.sw708e16.server.persistence.given;

import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.RouteState;
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
    private RouteState routeState;

    public GivenRoute withDriver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public GivenRoute withWaypoint(Waypoint waypoint) {
        this.waypoints.add(waypoints.size(), waypoint);
        return this;
    }

    public GivenRoute withWaypoints(Collection<Waypoint> waypoints) {
        this.waypoints.addAll(waypoints);
        return this;
    }

    public GivenRoute withVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public GivenRoute withRouteState(RouteState routeState) {
        this.routeState = routeState;
        return this;
    }

    public Route in(RouteDao routeDao) {
        Route route = new Route(waypoints, driver, vehicle);
        if (routeState != null)
            route.setRouteState(routeState);
        routeDao.add(route);
        driver.addRoute(route);
        vehicle.addRoute(route);
        return route;
    }
}
