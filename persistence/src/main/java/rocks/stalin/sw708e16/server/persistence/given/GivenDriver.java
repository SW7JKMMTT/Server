package rocks.stalin.sw708e16.server.persistence.given;

import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.persistence.DriverDao;

import java.util.ArrayList;
import java.util.Collection;

public final class GivenDriver {
    private User user;
    private Collection<Route> routes = new ArrayList<Route>();

    public GivenDriver() {
    }

    public GivenDriver withUser(User user) {
        this.user = user;
        return this;
    }

    public GivenDriver withPath(Route route) {
        this.routes.add(route);
        return this;
    }

    public GivenDriver withPaths(Collection<Route> routes) {
        this.routes.addAll(routes);
        return this;
    }

    public GivenDriver but() {
        return new GivenDriver().withUser(user).withPaths(routes);
    }

    public Driver in(DriverDao driverDao) {
        Driver driver = new Driver(user);
        driver.addPaths(routes);
        driverDao.add(driver);
        return driver;
    }
}
