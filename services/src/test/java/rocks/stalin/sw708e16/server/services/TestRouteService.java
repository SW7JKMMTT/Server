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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
        Collection<Route> allRoutes = routeService.getAllRoutes(null, null, null, null,null);

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
        Collection<Route> allRoutes = routeService.getAllRoutes(null, null, null, null,null);

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
        new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        new GivenWaypoint().withTimestamp(new Date(1L)).withLatitude(12.34).withLongitude(34.56).withRoute(r1).in(waypointDao);

        // Act
        Collection<Route> found = routeService.getAllRoutes(RouteState.ACTIVE, null, null, null,null);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(1, found.size());
        assertThat(found, hasItem(r1));
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
        new GivenWaypoint().withTimestamp(new Date(1L)).withLatitude(12.34).withLongitude(34.56).withRoute(r1).in(waypointDao);

        // Act
        Collection<Route> found = routeService.getAllRoutes(RouteState.CREATED, null, null, null,null);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(1, found.size());
        assertThat(found, hasItem(r2));
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

    @Test
    public void testModifyRoute_ChangingState() throws Exception {
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
        Waypoint w1 = new GivenWaypoint().withTimestamp(new Date(1L)).withLatitude(12.34).withLongitude(34.56).withRoute(r1).in(waypointDao);
        r1.addWaypoint(w1);

        RouteBuilder routeBuilder = new RouteBuilder();
        routeBuilder.setRouteState(RouteState.COMPLETE);

        // Act
        Route found = routeService.modifyRoute(r1.getId(), routeBuilder);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(RouteState.COMPLETE, found.getRouteState());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModifyRoute_RouteIdIdNull() throws Exception {
        // Arrange

        // Act
        routeService.modifyRoute(null, new RouteBuilder());

        // Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModifyRoute_NoRoutesToFind() throws Exception {
        // Arrange

        // Act
        routeService.modifyRoute(new ObjectId(), new RouteBuilder());

        // Assert
    }

    @Test
    public void testModifyRoute_ChangeDriver() throws Exception {
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
        User newUser = new GivenUser().withName("Jeffy", "Jeffysen").withUsername("Testyy").withPassword("hunter2").in(userDao);
        Driver newDriver = new GivenDriver().withUser(newUser).in(driverDao);
        RouteBuilder routeBuilder = new RouteBuilder();
        routeBuilder.setDriverid(newDriver.getId());

        // Act
        Route found = routeService.modifyRoute(route.getId(), routeBuilder);

        // Assert
        Assert.assertNotNull(route);
        Assert.assertEquals(newDriver, found.getDriver());
    }

    @Test
    public void testModifyRoute_ChangeVehicle() throws Exception {
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
        Vehicle newVehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Lort")
            .withVintage(2014)
            .withVin(new Vin("harambe"))
            .in(vehicleDao);
        RouteBuilder routeBuilder = new RouteBuilder();
        routeBuilder.setVehicleid(newVehicle.getId());

        // Act
        Route found = routeService.modifyRoute(route.getId(), routeBuilder);

        // Assert
        Assert.assertNotNull(route);
        Assert.assertEquals(newVehicle, found.getVehicle());
    }

    @Test
    public void testModifyRoute_ChangeVehicleAndDriver() throws Exception {
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
        Vehicle newVehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Lort")
            .withVintage(2014)
            .withVin(new Vin("harambe"))
            .in(vehicleDao);
        User newUser = new GivenUser().withName("Jeffy", "Jeffysen").withUsername("Testyy").withPassword("hunter2").in(userDao);
        Driver newDriver = new GivenDriver().withUser(newUser).in(driverDao);

        RouteBuilder routeBuilder = new RouteBuilder();
        routeBuilder.setVehicleid(newVehicle.getId());
        routeBuilder.setDriverid(newDriver.getId());

        // Act
        Route found = routeService.modifyRoute(route.getId(), routeBuilder);

        // Assert
        Assert.assertNotNull(route);
        Assert.assertEquals(newVehicle, found.getVehicle());
        Assert.assertEquals(newDriver, found.getDriver());
    }

    @Test
    public void testModifyRoute_ChangeVehicleAndDriverAndState() throws Exception {
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
        Vehicle newVehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Lort")
            .withVintage(2014)
            .withVin(new Vin("harambe"))
            .in(vehicleDao);
        User newUser = new GivenUser().withName("Jeffy", "Jeffysen").withUsername("Testyy").withPassword("hunter2").in(userDao);
        Driver newDriver = new GivenDriver().withUser(newUser).in(driverDao);

        RouteBuilder routeBuilder = new RouteBuilder();
        routeBuilder.setVehicleid(newVehicle.getId());
        routeBuilder.setDriverid(newDriver.getId());
        routeBuilder.setRouteState(RouteState.COMPLETE);

        // Act
        Route found = routeService.modifyRoute(route.getId(), routeBuilder);

        // Assert
        Assert.assertNotNull(route);
        Assert.assertEquals(newVehicle, found.getVehicle());
        Assert.assertEquals(newDriver, found.getDriver());
        Assert.assertEquals(RouteState.COMPLETE, found.getRouteState());
    }

    @Test
    public void testGetAllPaths_WithStateAndDriver() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Test").withPassword("lul").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Lort")
            .withVintage(1999)
            .withVin(new Vin("d"))
            .in(vehicleDao);
        Route r1 = new GivenRoute().withDriver(driver).withVehicle(vehicle).withRouteState(RouteState.ACTIVE).in(routeDao);
        new GivenRoute().withDriver(driver).withVehicle(vehicle).withRouteState(RouteState.CREATED).in(routeDao);

        // Act
        Collection<Route> found = routeService.getAllRoutes(RouteState.ACTIVE, driver.getId(), null, null,null);

        // Assert
        Assert.assertNotNull(found);
        assertThat(found, hasSize(1));
        assertThat(found, hasItem(r1));
    }

    @Test
    public void testGetAllPaths_WithDriver() throws Exception {
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
        new GivenWaypoint().withTimestamp(new Date(1L)).withLatitude(12.34).withLongitude(34.56).withRoute(r1).in(waypointDao);

        // Act
        Collection<Route> found = routeService.getAllRoutes(null, driver.getId(), null, null,null);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasSize(2));
        assertThat(found, hasItem(r1));
        assertThat(found, hasItem(r2));
    }

    @Test(expected = NotFoundException.class)
    public void testGetAllPaths_WithNoDriver() throws Exception {
        // Arrange

        // Act
        routeService.getAllRoutes(null, new ObjectId(), null, null,null);

        // Assert
    }
}
