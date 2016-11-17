package rocks.stalin.sw708e16.server.services;

import junit.framework.Assert;
import org.bson.types.ObjectId;
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
import rocks.stalin.sw708e16.server.persistence.DriverDao;
import rocks.stalin.sw708e16.server.persistence.RouteDao;
import rocks.stalin.sw708e16.server.persistence.UserDao;
import rocks.stalin.sw708e16.server.persistence.VehicleDao;
import rocks.stalin.sw708e16.server.persistence.given.GivenDriver;
import rocks.stalin.sw708e16.server.persistence.given.GivenRoute;
import rocks.stalin.sw708e16.server.persistence.given.GivenUser;
import rocks.stalin.sw708e16.server.persistence.given.GivenVehicle;
import rocks.stalin.sw708e16.server.services.builders.RouteBuilder;
import rocks.stalin.sw708e16.test.DatabaseTest;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.Collection;

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

    @Test
    public void testGetAllPaths_Empty() throws Exception {
        // Arrange

        // Act
        Collection<Route> allRoutes = routeService.getAllRoutes();

        // Assert
        Assert.assertNotNull(allRoutes);
        Assert.assertTrue(allRoutes.isEmpty());
    }

    @Test
    public void testGetAllPaths() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Test").withPassword("lul").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
                .withMake("Ford")
                .withModel("Lort")
                .withVintage(1999)
                .withVin(new Vin("d"))
                .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);

        // Act
        Collection<Route> allRoutes = routeService.getAllRoutes();

        // Assert
        Assert.assertNotNull(allRoutes);
        Assert.assertTrue(allRoutes.size() == 1);
        Assert.assertTrue(allRoutes.contains(route));
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
        User user = new GivenUser().withName("Test").withPassword("lul").in(userDao);
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
        User testUser = new GivenUser().withName("Jesper").withPassword("Jensen").in(userDao);
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
        User testUser = new GivenUser().withName("Jesper").withPassword("Jensen").in(userDao);
        RouteBuilder routeBuilder = new RouteBuilder();

        // Act
        routeService.createRoute(testUser, routeBuilder);

        // Assert
        // expected exception
    }

    @Test(expected = BadRequestException.class)
    public void testCreateRoute_RouteBuilderMissingData() throws Exception {
        // Arrange
        User testUser = new GivenUser().withName("Jesper").withPassword("Jensen").in(userDao);

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
        User testUser = new GivenUser().withName("Jesper").withPassword("Jensen").in(userDao);
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
