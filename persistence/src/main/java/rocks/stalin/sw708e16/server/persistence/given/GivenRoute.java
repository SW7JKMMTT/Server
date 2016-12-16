package rocks.stalin.sw708e16.server.persistence.given;

import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.RouteState;
import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.persistence.RouteDao;

public class GivenRoute {
    private Driver driver;
    private Vehicle vehicle;
    private RouteState routeState;

    public GivenRoute withDriver(Driver driver) {
        this.driver = driver;
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
        Route route = new Route(driver, vehicle);
        if (routeState != null)
            route.setRouteState(routeState);
        routeDao.add(route);
        driver.addRoute(route);
        vehicle.addRoute(route);
        return route;
    }
}
