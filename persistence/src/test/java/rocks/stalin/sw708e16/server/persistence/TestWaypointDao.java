package rocks.stalin.sw708e16.server.persistence;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.Vin;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;
import rocks.stalin.sw708e16.server.persistence.given.*;
import rocks.stalin.sw708e16.test.SpatialDatabaseTest;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dao-config.xml"})
@Transactional
public class TestWaypointDao extends SpatialDatabaseTest {
    @Autowired
    WaypointDao waypointDao;

    @Autowired
    UserDao userDao;

    @Autowired
    RouteDao routeDao;

    @Autowired
    VehicleDao vehicleDao;

    @Autowired
    DriverDao driverDao;

    @Test
    public void testWithinRadius_WithTwo_FindTwo() throws Exception {
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint way = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(1)
            .withLongitude(1)
            .withTimestamp(new Date())
            .in(waypointDao);
        Waypoint way2 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(1.5)
            .withLongitude(1)
            .withTimestamp(new Date())
            .in(waypointDao);

        flushIndex();

        List<Waypoint> ret = waypointDao.withinRadius(new Coordinate(1.0, 1.0), 100);

        Assert.assertThat(ret, hasSize(2));
        Assert.assertThat(ret, hasItem(way));
        Assert.assertThat(ret, hasItem(way2));
    }

    @Test
    public void testWithinRadius_WithTwo_FindOne() throws Exception {
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint way = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(1)
            .withLongitude(1)
            .withTimestamp(new Date())
            .in(waypointDao);
        new GivenWaypoint()
            .withRoute(route)
            .withLatitude(1.5)
            .withLongitude(1)
            .withTimestamp(new Date())
            .in(waypointDao);

        flushIndex();

        List<Waypoint> ret = waypointDao.withinRadius(new Coordinate(1.0, 1.0), 40);

        Assert.assertThat(ret, hasSize(1));
        Assert.assertThat(ret, hasItem(way));
    }

    @Test
    public void testWithinRadius_WithTwo_FindNone() throws Exception {
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        new GivenWaypoint()
            .withRoute(route)
            .withLatitude(40.71)
            .withLongitude(-74.01)
            .withTimestamp(new Date())
            .in(waypointDao);
        new GivenWaypoint()
            .withRoute(route)
            .withLatitude(57)
            .withLongitude(10)
            .withTimestamp(new Date())
            .in(waypointDao);

        flushIndex();

        List<Waypoint> ret = waypointDao.withinRadius(new Coordinate(0.0, 0.0) , 100);

        Assert.assertThat(ret, hasSize(0));
    }

    @Test
    public void testWithinRadius_WithNone_FindNone() throws Exception {
        List<Waypoint> ret = waypointDao.withinRadius(new Coordinate(0.0, 0.0), 100);

        Assert.assertThat(ret, hasSize(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithinRadius_InvalidLatitude() throws Exception {
        waypointDao.withinRadius(new Coordinate(100.0, 1.0), 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithinRadius_InvalidLongitude() throws Exception {
        waypointDao.withinRadius(new Coordinate(10.0, 190.0), 100);
    }

    @Test
    public void testWithMaximum_WithWaypointInRoute_OneWaypoint() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint waypoint = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(57)
            .withLongitude(10)
            .withTimestamp(new Date(1))
            .in(waypointDao);
        new GivenWaypoint()
            .withRoute(route)
            .withLatitude(40.71)
            .withLongitude(-74.01)
            .withTimestamp(new Date(2))
            .in(waypointDao);

        // Act
        List<Waypoint> found = waypointDao.byRoute(route, 1);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasItem(waypoint));
        assertThat(found, hasSize(1));
    }

    @Test
    public void testWithMaximum_WithWaypointInRoute_TestingForDifferentRoute() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Route otherRoute = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint waypoint = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(57)
            .withLongitude(10)
            .withTimestamp(new Date(2))
            .in(waypointDao);
        new GivenWaypoint()
            .withRoute(route)
            .withLatitude(40.71)
            .withLongitude(-74.01)
            .withTimestamp(new Date(1))
            .in(waypointDao);

        // Act
        List<Waypoint> found = waypointDao.byRoute(otherRoute, 1);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasSize(0));
    }

    @Test
    public void testWithMaximum_WithWaypointInRoute_TwoWaypoints() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint w1 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(57)
            .withLongitude(10)
            .withTimestamp(new Date(1))
            .in(waypointDao);
        Waypoint w2 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(40.71)
            .withLongitude(-74.01)
            .withTimestamp(new Date(2))
            .in(waypointDao);

        // Act
        List<Waypoint> found = waypointDao.byRoute(route, 2);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasItem(w1));
        assertThat(found, hasItem(w2));
        assertThat(found, hasSize(2));
        // Check the order of the items.
        assertThat(found, contains(w1, w2));
    }

    @Test
    public void testWithMaximum_WithWaypointInRoute_MaxHigherThanItemCount() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint w1 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(57)
            .withLongitude(10)
            .withTimestamp(new Date(1))
            .in(waypointDao);
        Waypoint w2 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(40.71)
            .withLongitude(-74.01)
            .withTimestamp(new Date(2))
            .in(waypointDao);

        // Act
        List<Waypoint> found = waypointDao.byRoute(route, 5);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasItem(w1));
        assertThat(found, hasItem(w2));
        assertThat(found, hasSize(2));
        // Check the order of the items.
        assertThat(found, contains(w1, w2));
    }

    @Test
    public void testSince_WithWaypointInRoute_SinceBeforeFirst() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint w1 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(57)
            .withLongitude(10)
            .withTimestamp(new Date(1))
            .in(waypointDao);
        Waypoint w2 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(40.71)
            .withLongitude(-74.01)
            .withTimestamp(new Date(2))
            .in(waypointDao);

        // Act
        List<Waypoint> found = waypointDao.byRoute_after(route, new Date(0));

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasItem(w1));
        assertThat(found, hasItem(w2));
        assertThat(found, hasSize(2));
        // Check the order of the items.
        assertThat(found, contains(w1, w2));
    }

    @Test
    public void testSince_WithWaypointInRoute_SinceSameAsFirst() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint w1 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(57)
            .withLongitude(10)
            .withTimestamp(new Date(1))
            .in(waypointDao);
        Waypoint w2 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(40.71)
            .withLongitude(-74.01)
            .withTimestamp(new Date(2))
            .in(waypointDao);

        // Act
        List<Waypoint> found = waypointDao.byRoute_after(route, new Date(1));

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasItem(w2));
        assertThat(found, hasSize(1));
    }

    @Test
    public void testSince_WithWaypointInRoute_SameAsNewest() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint w1 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(57)
            .withLongitude(10)
            .withTimestamp(new Date(1))
            .in(waypointDao);
        Waypoint w2 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(40.71)
            .withLongitude(-74.01)
            .withTimestamp(new Date(2))
            .in(waypointDao);

        // Act
        List<Waypoint> found = waypointDao.byRoute_after(route, new Date(2));

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasSize(0));
    }

    @Test
    public void testSince_WithWaypointInRoute_DifferentRoute() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Route otherRoute = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        new GivenWaypoint()
            .withRoute(route)
            .withLatitude(57)
            .withLongitude(10)
            .withTimestamp(new Date(1))
            .in(waypointDao);
        new GivenWaypoint()
            .withRoute(route)
            .withLatitude(40.71)
            .withLongitude(-74.01)
            .withTimestamp(new Date(2))
            .in(waypointDao);

        // Act
        List<Waypoint> found = waypointDao.byRoute_after(otherRoute, new Date(0));

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasSize(0));
    }

    @Test
    public void testSinceWithMaximum_WithWaypointInRoute_DifferentRoute() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Route otherRoute = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        new GivenWaypoint()
            .withRoute(route)
            .withLatitude(57)
            .withLongitude(10)
            .withTimestamp(new Date(1))
            .in(waypointDao);
        new GivenWaypoint()
            .withRoute(route)
            .withLatitude(40.71)
            .withLongitude(-74.01)
            .withTimestamp(new Date(2))
            .in(waypointDao);

        // Act
        List<Waypoint> found = waypointDao.byRoute_afterWithMaximum(otherRoute, new Date(0),2);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasSize(0));
    }

    @Test
    public void testSinceWithMaximum_WithWaypointInRoute_SinceBeforeFirstButWithMaximum() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint w1 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(57)
            .withLongitude(10)
            .withTimestamp(new Date(1))
            .in(waypointDao);
        Waypoint w2 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(40.71)
            .withLongitude(-74.01)
            .withTimestamp(new Date(2))
            .in(waypointDao);

        // Act
        List<Waypoint> found = waypointDao.byRoute_afterWithMaximum(route, new Date(0), 1);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasItem(w1));
        assertThat(found, hasSize(1));
    }

    @Test
    public void testSinceWithMaximum_WithWaypointInRoute_SinceBeforeFirstButWithHighMaximum() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint w1 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(57)
            .withLongitude(10)
            .withTimestamp(new Date(1))
            .in(waypointDao);
        Waypoint w2 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(40.71)
            .withLongitude(-74.01)
            .withTimestamp(new Date(2))
            .in(waypointDao);

        // Act
        List<Waypoint> found = waypointDao.byRoute_afterWithMaximum(route, new Date(0), 10);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasItem(w1));
        assertThat(found, hasItem(w2));
        assertThat(found, hasSize(2));
        // Check the order of the items.
        assertThat(found, contains(w1, w2));
    }

    @Test
    public void testSinceWithMaximum_WithWaypointInRoute_HighMaximumSinceFirst() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint w1 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(57)
            .withLongitude(10)
            .withTimestamp(new Date(1))
            .in(waypointDao);
        Waypoint w2 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(40.71)
            .withLongitude(-74.01)
            .withTimestamp(new Date(2))
            .in(waypointDao);

        // Act
        List<Waypoint> found = waypointDao.byRoute_afterWithMaximum(route, new Date(1), 10);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasItem(w2));
        assertThat(found, hasSize(1));
    }


}
