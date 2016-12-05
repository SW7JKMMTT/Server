package rocks.stalin.sw708e16.server.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.*;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.persistence.*;
import rocks.stalin.sw708e16.server.persistence.given.*;
import rocks.stalin.sw708e16.test.SpatialDatabaseTest;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.Collection;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-config.xml"})
@Transactional
public class TestSpatialRouteService extends SpatialDatabaseTest {
    @Autowired
    private RouteDao routeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DriverDao driverDao;

    @Autowired
    private VehicleDao vehicleDao;

    @Autowired
    private RouteService routeService;

    @Autowired
    private WaypointDao waypointDao;

    @Test
    public void testGetAll_Spatial_Valid() throws Exception {
        // Arrange
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("fuck").in(userDao);
        Driver jeffD = new GivenDriver().withUser(jeff).in(driverDao);
        Vehicle jeffCar = new GivenVehicle().withMake("Ford").withModel("Escort").withVin(new Vin("ads")).withVintage(1992).in(vehicleDao);

        User jeffy = new GivenUser().withName("Jeffy", "Jeffysen").withUsername("Jeffy").withPassword("fuck").in(userDao);
        Driver jeffyD = new GivenDriver().withUser(jeffy).in(driverDao);
        Vehicle jeffyCar = new GivenVehicle().withMake("Coorporate").withModel("Escort").withVin(new Vin("aids")).withVintage(1999).in(vehicleDao);

        Route r1 = new GivenRoute().withDriver(jeffD).withVehicle(jeffCar).in(routeDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.0).withLongitude(10.0).withTimestamp(new Date(1)).in(waypointDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.1).withLongitude(10.1).withTimestamp(new Date(2)).in(waypointDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.2).withLongitude(10.2).withTimestamp(new Date(3)).in(waypointDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.3).withLongitude(10.3).withTimestamp(new Date(4)).in(waypointDao);

        Route r2 = new GivenRoute().withDriver(jeffyD).withVehicle(jeffyCar).in(routeDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.0).withLongitude(11.0).withTimestamp(new Date(1)).in(waypointDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.1).withLongitude(11.1).withTimestamp(new Date(2)).in(waypointDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.2).withLongitude(11.2).withTimestamp(new Date(3)).in(waypointDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.3).withLongitude(11.3).withTimestamp(new Date(4)).in(waypointDao);

        // Ensure waypoints are written to the index before querying it.
        flushIndex();

        // Act
        Collection<Route> found = routeService.getAllRoutes(null, null, 56.0, 10.0, 10000.0);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasItem(r1));
        assertThat(found, hasItem(r2));
    }

    @Test
    public void testGetAll_Spatial_ValidWithState() throws Exception {
        // Arrange
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("fuck").in(userDao);
        Driver jeffD = new GivenDriver().withUser(jeff).in(driverDao);
        Vehicle jeffCar = new GivenVehicle().withMake("Ford").withModel("Escort").withVin(new Vin("ads")).withVintage(1992).in(vehicleDao);

        User jeffy = new GivenUser().withName("Jeffy", "Jeffysen").withUsername("Jeffy").withPassword("fuck").in(userDao);
        Driver jeffyD = new GivenDriver().withUser(jeffy).in(driverDao);
        Vehicle jeffyCar = new GivenVehicle().withMake("Coorporate").withModel("Escort").withVin(new Vin("aids")).withVintage(1999).in(vehicleDao);

        Route r1 = new GivenRoute().withDriver(jeffD).withVehicle(jeffCar).in(routeDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.0).withLongitude(10.0).withTimestamp(new Date(1)).in(waypointDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.1).withLongitude(10.1).withTimestamp(new Date(2)).in(waypointDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.2).withLongitude(10.2).withTimestamp(new Date(3)).in(waypointDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.3).withLongitude(10.3).withTimestamp(new Date(4)).in(waypointDao);

        Route r2 = new GivenRoute().withDriver(jeffyD).withVehicle(jeffyCar).in(routeDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.0).withLongitude(11.0).withTimestamp(new Date(1)).in(waypointDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.1).withLongitude(11.1).withTimestamp(new Date(2)).in(waypointDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.2).withLongitude(11.2).withTimestamp(new Date(3)).in(waypointDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.3).withLongitude(11.3).withTimestamp(new Date(4)).in(waypointDao);
        r2.setRouteState(RouteState.COMPLETE);

        // Ensure waypoints are written to the index before querying it.
        flushIndex();

        // Act
        Collection<Route> found = routeService.getAllRoutes(RouteState.COMPLETE, null, 56.0, 10.0, 10000.0);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, not(hasItem(r1)));
        assertThat(found, hasItem(r2));
    }

    @Test
    public void testGetAll_Spatial_ValidWithDriver() throws Exception {
        // Arrange
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("fuck").in(userDao);
        Driver jeffD = new GivenDriver().withUser(jeff).in(driverDao);
        Vehicle jeffCar = new GivenVehicle().withMake("Ford").withModel("Escort").withVin(new Vin("ads")).withVintage(1992).in(vehicleDao);

        User jeffy = new GivenUser().withName("Jeffy", "Jeffysen").withUsername("Jeffy").withPassword("fuck").in(userDao);
        Driver jeffyD = new GivenDriver().withUser(jeffy).in(driverDao);
        Vehicle jeffyCar = new GivenVehicle().withMake("Coorporate").withModel("Escort").withVin(new Vin("aids")).withVintage(1999).in(vehicleDao);

        Route r1 = new GivenRoute().withDriver(jeffD).withVehicle(jeffCar).in(routeDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.0).withLongitude(10.0).withTimestamp(new Date(1)).in(waypointDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.1).withLongitude(10.1).withTimestamp(new Date(2)).in(waypointDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.2).withLongitude(10.2).withTimestamp(new Date(3)).in(waypointDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.3).withLongitude(10.3).withTimestamp(new Date(4)).in(waypointDao);

        Route r2 = new GivenRoute().withDriver(jeffyD).withVehicle(jeffyCar).in(routeDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.0).withLongitude(11.0).withTimestamp(new Date(1)).in(waypointDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.1).withLongitude(11.1).withTimestamp(new Date(2)).in(waypointDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.2).withLongitude(11.2).withTimestamp(new Date(3)).in(waypointDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.3).withLongitude(11.3).withTimestamp(new Date(4)).in(waypointDao);

        // Ensure waypoints are written to the index before querying it.
        flushIndex();

        // Act
        Collection<Route> found = routeService.getAllRoutes(null, jeffD.getId(), 56.0, 10.0, 10000.0);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasItem(r1));
        assertThat(found, not(hasItem(r2)));
    }

    @Test
    public void testGetAll_Spatial_ValidWithDriverAndState() throws Exception {
        // Arrange
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("fuck").in(userDao);
        Driver jeffD = new GivenDriver().withUser(jeff).in(driverDao);
        Vehicle jeffCar = new GivenVehicle().withMake("Ford").withModel("Escort").withVin(new Vin("ads")).withVintage(1992).in(vehicleDao);

        User jeffy = new GivenUser().withName("Jeffy", "Jeffysen").withUsername("Jeffy").withPassword("fuck").in(userDao);
        Driver jeffyD = new GivenDriver().withUser(jeffy).in(driverDao);
        Vehicle jeffyCar = new GivenVehicle().withMake("Coorporate").withModel("Escort").withVin(new Vin("aids")).withVintage(1999).in(vehicleDao);

        Route r1 = new GivenRoute().withDriver(jeffD).withVehicle(jeffCar).in(routeDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.0).withLongitude(10.0).withTimestamp(new Date(1)).in(waypointDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.1).withLongitude(10.1).withTimestamp(new Date(2)).in(waypointDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.2).withLongitude(10.2).withTimestamp(new Date(3)).in(waypointDao);
        new GivenWaypoint().withRoute(r1).withLatitude(56.3).withLongitude(10.3).withTimestamp(new Date(4)).in(waypointDao);

        Route r2 = new GivenRoute().withDriver(jeffyD).withVehicle(jeffyCar).in(routeDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.0).withLongitude(11.0).withTimestamp(new Date(1)).in(waypointDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.1).withLongitude(11.1).withTimestamp(new Date(2)).in(waypointDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.2).withLongitude(11.2).withTimestamp(new Date(3)).in(waypointDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.3).withLongitude(11.3).withTimestamp(new Date(4)).in(waypointDao);
        r2.setRouteState(RouteState.COMPLETE);

        // Ensure waypoints are written to the index before querying it.
        flushIndex();

        // Act
        Collection<Route> found = routeService.getAllRoutes(RouteState.COMPLETE, jeffyD.getId(), 56.0, 10.0, 10000.0);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, not(hasItem(r1)));
        assertThat(found, hasItem(r2));
    }

    @Test
    public void testGetAll_Spatial_ResultEmpty() throws Exception {
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
        Collection<Route> found = routeService.getAllRoutes(null, null, 10.0, 10.0, 1000.0);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found.isEmpty(), is(true));
    }

    @Test
    public void testGetAll_Spatial_EmptySetup() throws Exception {
        // Arrange

        // Act
        Collection<Route> found = routeService.getAllRoutes(null, null, 10.0, 10.0, 1000.0);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found.isEmpty(), is(true));
    }

    @Test(expected = BadRequestException.class)
    public void testGetAll_Spatial_InvalidLongitude() throws Exception {
        // Arrange

        // Act
        routeService.getAllRoutes(null, null, 100.0, 10.0, 10000.0);

        // Assert
    }

    @Test(expected = BadRequestException.class)
    public void testGetAll_Spatial_InvalidLatitude() throws Exception {
        // Arrange

        // Act
        routeService.getAllRoutes(null, null, 56.0, -200.0, 10000.0);

        // Assert
    }

    @Test(expected = BadRequestException.class)
    public void testGetAll_Spatial_InvalidRadius() throws Exception {
        // Arrange

        // Act
        routeService.getAllRoutes(null, null, 56.0, -2.0, -0.1);

        // Assert
    }

    @Test(expected = BadRequestException.class)
    public void testGetAll_Spatial_NullLongitude() throws Exception {
        // Arrange

        // Act
        routeService.getAllRoutes(null, null, 56.0, null, 10000.0);

        // Assert
    }

    @Test(expected = BadRequestException.class)
    public void testGetAll_Spatial_NullLatitude() throws Exception {
        // Arrange

        // Act
        routeService.getAllRoutes(null, null, null, 10.0, 10000.0);

        // Assert
    }

    @Test(expected = BadRequestException.class)
    public void testGetAll_Spatial_NullRadius() throws Exception {
        // Arrange

        // Act
        routeService.getAllRoutes(null, null, 56.0, -2.0, null);

        // Assert
    }

    @Test(expected = NotFoundException.class)
    public void testGetAll_Spatial_DriverDoesNotExist() throws Exception {
        // Arrange

        // Act
        routeService.getAllRoutes(null, new Long(-1), 56.0, -2.0, 100.0);

        // Assert
    }
}
