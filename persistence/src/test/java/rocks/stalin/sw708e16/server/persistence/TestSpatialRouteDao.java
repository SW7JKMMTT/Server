package rocks.stalin.sw708e16.server.persistence;

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
import rocks.stalin.sw708e16.server.persistence.given.*;
import rocks.stalin.sw708e16.test.SpatialDatabaseTest;

import java.util.Collection;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dao-config.xml"})
@Transactional
public class TestSpatialRouteDao extends SpatialDatabaseTest {
    @Autowired
    private RouteDao routeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DriverDao driverDao;

    @Autowired
    private VehicleDao vehicleDao;

    @Autowired
    private WaypointDao waypointDao;

    @Test
    public void testWithinRadius_OneRouteMultipleWaypoints_OneResult() throws Exception {
        // Arrange
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("fuck").in(userDao);
        Driver jeffD = new GivenDriver().withUser(jeff).in(driverDao);
        Vehicle jeffCar = new GivenVehicle().withMake("Ford").withModel("Escort").withVin(new Vin("ads")).withVintage(1992).in(vehicleDao);

        Route r1 = new GivenRoute().withDriver(jeffD).withVehicle(jeffCar).in(routeDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.0).withLongitude(10.0).withTimestamp(new Date(1)).in(waypointDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.1).withLongitude(10.1).withTimestamp(new Date(2)).in(waypointDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.2).withLongitude(10.2).withTimestamp(new Date(3)).in(waypointDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.3).withLongitude(10.3).withTimestamp(new Date(4)).in(waypointDao);

        // Ensure waypoints are written to the index before querying it.
        flushIndex();

        // Act
        Collection<Route> found = routeDao.withinRadius_ForDisplay(waypointDao, new Coordinate(56.3, 10.3), 0.1);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasItem(r1));
    }

    @Test
    public void testWithinRadius_MultipleWaypoints_OneResult() throws Exception {
        // Arrange
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("fuck").in(userDao);
        Driver jeffD = new GivenDriver().withUser(jeff).in(driverDao);
        Vehicle jeffCar = new GivenVehicle().withMake("Ford").withModel("Escort").withVin(new Vin("ads")).withVintage(1992).in(vehicleDao);

        Route r1 = new GivenRoute().withDriver(jeffD).withVehicle(jeffCar).in(routeDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.0).withLongitude(10.0).withTimestamp(new Date(1)).in(waypointDao);

        Route r2 = new GivenRoute().withDriver(jeffD).withVehicle(jeffCar).in(routeDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.0).withLongitude(11.0).withTimestamp(new Date(1)).in(waypointDao);

        // Ensure waypoints are written to the index before querying it.
        flushIndex();

        // Act
        Collection<Route> found = routeDao.withinRadius_ForDisplay(waypointDao, new Coordinate(56.0, 10.0), 1.0);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasItem(r1));
        assertThat(found, not(hasItem(r2)));
    }

    @Test
    public void testWithinRadius_MultipleWaypoints_MultipleResults() throws Exception {
        // Arrange
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("fuck").in(userDao);
        Driver jeffD = new GivenDriver().withUser(jeff).in(driverDao);
        Vehicle jeffCar = new GivenVehicle().withMake("Ford").withModel("Escort").withVin(new Vin("ads")).withVintage(1992).in(vehicleDao);

        Route r1 = new GivenRoute().withDriver(jeffD).withVehicle(jeffCar).in(routeDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.0).withLongitude(10.0).withTimestamp(new Date(1)).in(waypointDao);

        Route r2 = new GivenRoute().withDriver(jeffD).withVehicle(jeffCar).in(routeDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.0).withLongitude(11.0).withTimestamp(new Date(1)).in(waypointDao);

        // Ensure waypoints are written to the index before querying it.
        flushIndex();

        // Act
        Collection<Route> found = routeDao.withinRadius_ForDisplay(waypointDao, new Coordinate(56.0, 10.0), 500.0);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasItem(r1));
        assertThat(found, hasItem(r2));
    }

    @Test
    public void testWithinRadius_WrappingAroundGlobeLatitude() throws Exception {
        // Arrange
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("fuck").in(userDao);
        Driver jeffD = new GivenDriver().withUser(jeff).in(driverDao);
        Vehicle jeffCar = new GivenVehicle().withMake("Ford").withModel("Escort").withVin(new Vin("ads")).withVintage(1992).in(vehicleDao);

        Route r1 = new GivenRoute().withDriver(jeffD).withVehicle(jeffCar).in(routeDao);
        new GivenWaypoint().withRoute(r1).withLatitude(89.0).withLongitude(90.0).withTimestamp(new Date(1)).in(waypointDao);

        Route r2 = new GivenRoute().withDriver(jeffD).withVehicle(jeffCar).in(routeDao);
        new GivenWaypoint().withRoute(r2).withLatitude(89.0).withLongitude(-90.0).withTimestamp(new Date(1)).in(waypointDao);

        // Distance between them is ~223km

        // Ensure waypoints are written to the index before querying it.
        flushIndex();

        // Act
        Collection<Route> found = routeDao.withinRadius_ForDisplay(waypointDao, new Coordinate(89.0, 0.0), 500.0);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasItem(r1));
        assertThat(found, hasItem(r2));
    }

    @Test
    public void testWithinRadius_WrappingAroundGlobeLongitude() throws Exception {
        // Arrange
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("fuck").in(userDao);
        Driver jeffD = new GivenDriver().withUser(jeff).in(driverDao);
        Vehicle jeffCar = new GivenVehicle().withMake("Ford").withModel("Escort").withVin(new Vin("ads")).withVintage(1992).in(vehicleDao);

        Route r1 = new GivenRoute().withDriver(jeffD).withVehicle(jeffCar).in(routeDao);
        new GivenWaypoint().withRoute(r1).withLatitude(0.0).withLongitude(179.0).withTimestamp(new Date(1)).in(waypointDao);

        Route r2 = new GivenRoute().withDriver(jeffD).withVehicle(jeffCar).in(routeDao);
        new GivenWaypoint().withRoute(r2).withLatitude(0.0).withLongitude(-179.0).withTimestamp(new Date(1)).in(waypointDao);

        // Distance between them is ~222km

        // Ensure waypoints are written to the index before querying it.
        flushIndex();

        // Act
        Collection<Route> found = routeDao.withinRadius_ForDisplay(waypointDao, new Coordinate(0.0, 179.5), 250.0);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasItem(r1));
        assertThat(found, hasItem(r2));
    }
}
