package rocks.stalin.sw708e16.server.services;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.*;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;
import rocks.stalin.sw708e16.server.persistence.*;
import rocks.stalin.sw708e16.server.persistence.given.*;
import rocks.stalin.sw708e16.server.services.builders.RouteBuilder;
import rocks.stalin.sw708e16.test.DatabaseTest;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.Collection;
import java.util.Date;

import static org.hamcrest.core.IsCollectionContaining.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-config.xml"})
@Transactional
public class TestRouteService extends DatabaseTest {
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
    public void testGetAllPaths_EmptyWithoutState() throws Exception {
        // Arrange

        // Act
        Collection<Route> allRoutes = routeService.getAllRoutes(null);

        // Assert
        Assert.assertNotNull(allRoutes);
        Assert.assertTrue(allRoutes.isEmpty());
    }

    @Test
    public void testGetAllPaths_WithoutState() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Test").withPassword("lul").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
                .withMake("Ford")
                .withModel("Lort")
                .withVintage(1999)
                .withVin(new Vin("d"))
                .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);

        // Act
        Collection<Route> allRoutes = routeService.getAllRoutes(null);

        // Assert
        Assert.assertNotNull(allRoutes);
        Assert.assertTrue(allRoutes.size() == 1);
        Assert.assertTrue(allRoutes.contains(route));
    }

    @Test
    public void testGetAllPaths_WithStateFindActive() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Test").withPassword("lul").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Lort")
            .withVintage(1999)
            .withVin(new Vin("d"))
            .in(vehicleDao);
        Route r1 = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Route r2 = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint w1 = new GivenWaypoint().withTimestamp(new Date(1L)).withLatitude(12.34).withLongitude(34.56).withRoute(r1).in(waypointDao);
        r1.addWaypoint(w1);
        Waypoint w2 = new GivenWaypoint().withTimestamp(new Date(2L)).withLatitude(12.34).withLongitude(34.56).withRoute(r1).in(waypointDao);
        r1.addWaypoint(w2);

        // Act
        Collection<Route> found = routeService.getAllRoutes(RouteState.ACTIVE);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(1, found.size());
        Assert.assertThat(found, hasItem(r1));
    }

    @Test
    public void testGetAllPaths_WithStateFindCreated() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Test").withPassword("lul").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Lort")
            .withVintage(1999)
            .withVin(new Vin("d"))
            .in(vehicleDao);
        Route r1 = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Route r2 = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint w1 = new GivenWaypoint().withTimestamp(new Date(1L)).withLatitude(12.34).withLongitude(34.56).withRoute(r1).in(waypointDao);
        r1.addWaypoint(w1);
        Waypoint w2 = new GivenWaypoint().withTimestamp(new Date(2L)).withLatitude(12.34).withLongitude(34.56).withRoute(r1).in(waypointDao);
        r1.addWaypoint(w2);

        // Act
        Collection<Route> found = routeService.getAllRoutes(RouteState.CREATED);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(1, found.size());
        Assert.assertThat(found, hasItem(r2));
    }



    @Test(expected = NotFoundException.class)
    public void testGetById_NotExists() throws Exception {
        // Arrange
        ObjectId objectId = new ObjectId();

        // Act
        Route found = routeService.getRouteById(objectId);

        // Assert
        Assert.assertNull(found);
    }

    @Test
    public void testGetById() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Test").withPassword("lul").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
                .withMake("Ford")
                .withModel("Lort")
                .withVintage(1999)
                .withVin(new Vin("d"))
                .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);

        // Act
        Route found = routeService.getRouteById(route.getId());

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(route, found);
    }

    @Test
    public void testCreateRoute_UserIsNoDriver() throws Exception {
        // Arrange
        Vehicle vehicle = new GivenVehicle()
                .withMake("Ford")
                .withModel("Lort")
                .withVintage(1999)
                .withVin(new Vin("d"))
                .in(vehicleDao);
        User testUser = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jesper").withPassword("Jensen").in(userDao);
        RouteBuilder routeBuilder = new RouteBuilder();
        routeBuilder.setVehicleid(vehicle.getId());

        // Act
        Route created = routeService.createRoute(testUser, routeBuilder);

        // Assert
        Assert.assertNotNull(created);
        Assert.assertEquals(created, routeDao.byId(created.getId()));
        Assert.assertNotNull(created.getDriver());
        Assert.assertNotNull(created.getDriver().getUser());
        Assert.assertEquals(created.getDriver().getUser(), testUser);
    }

    @Test(expected = BadRequestException.class)
    public void testCreateRoute_InvalidRouteBuilder() throws Exception {
        // Arrange
        User testUser = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jesper").withPassword("Jensen").in(userDao);
        RouteBuilder routeBuilder = new RouteBuilder();

        // Act
        routeService.createRoute(testUser, routeBuilder);

        // Assert
        // expected exception
    }

    @Test(expected = BadRequestException.class)
    public void testCreateRoute_RouteBuilderMissingData() throws Exception {
        // Arrange
        User testUser = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jesper").withPassword("Jensen").in(userDao);

        // Act
        routeService.createRoute(testUser, null);

        // Assert

    }

    @Test
    public void testCreateRoute_UserIsDriver() throws Exception {
        // Arrange
        Vehicle vehicle = new GivenVehicle()
                .withMake("Ford")
                .withModel("Lort")
                .withVintage(1999)
                .withVin(new Vin("d"))
                .in(vehicleDao);
        User testUser = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jesper").withPassword("Jensen").in(userDao);
        Driver driver = new GivenDriver().withUser(testUser).in(driverDao);
        testUser.setDriver(driver);
        RouteBuilder routeBuilder = new RouteBuilder();
        routeBuilder.setVehicleid(vehicle.getId());

        // Act
        Route created = routeService.createRoute(testUser, routeBuilder);

        // Assert
        Assert.assertNotNull(created);
        Assert.assertEquals(created, routeDao.byId(created.getId()));
        Assert.assertNotNull(created.getDriver());
        Assert.assertNotNull(created.getDriver().getUser());
        Assert.assertEquals(created.getDriver().getUser(), testUser);
        Assert.assertEquals(driver, testUser.getDriver());
    }
}
