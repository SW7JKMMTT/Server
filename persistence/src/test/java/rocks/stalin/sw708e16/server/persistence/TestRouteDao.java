package rocks.stalin.sw708e16.server.persistence;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.*;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.persistence.given.GivenDriver;
import rocks.stalin.sw708e16.server.persistence.given.GivenRoute;
import rocks.stalin.sw708e16.server.persistence.given.GivenUser;
import rocks.stalin.sw708e16.server.persistence.given.GivenVehicle;
import rocks.stalin.sw708e16.test.DatabaseTest;

import java.util.Collection;

import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
// Load the beans to configure, here the DAOs
@ContextConfiguration(locations = {"classpath:dao-config.xml"})
// apply the transaction manager to the test class so every DAO methods are executed
// within a transaction
@Transactional
public class TestRouteDao extends DatabaseTest {
    @Autowired
    private RouteDao routeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DriverDao driverDao;

    @Autowired
    private VehicleDao vehicleDao;

    @Test
    public void testGetAll_Empty() throws Exception {
        // Arrange

        // Act
        Collection<Route> allRoutes = routeDao.getAll_ForDisplay();

        // Assert
        Assert.assertNotNull(allRoutes);
        Assert.assertTrue(allRoutes.isEmpty());
    }

    @Test
    public void testGetAll() throws Exception {
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
        Collection<Route> allRoutes = routeDao.getAll_ForDisplay();

        // Assert
        Assert.assertNotNull(allRoutes);
        Assert.assertTrue(allRoutes.size() == 1);
        Assert.assertTrue(allRoutes.contains(route));
    }

    @Test
    public void testGetByState_ForDisplay_Empty() throws Exception {
        // Arrange

        // Act
        Collection<Route> routes = routeDao.getByState_ForDisplay(RouteState.CREATED);

        // Assert
        Assert.assertNotNull(routes);
        Assert.assertThat(routes, hasSize(0));
    }

    @Test
    public void testGetByState_ForDisplay_OnlySome() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Test").withPassword("lul").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Lort")
            .withVintage(1999)
            .withVin(new Vin("d"))
            .in(vehicleDao);
        Route r1 = new GivenRoute().withDriver(driver).withVehicle(vehicle).withRouteState(RouteState.CREATED).in(routeDao);
        Route r2 = new GivenRoute().withDriver(driver).withVehicle(vehicle).withRouteState(RouteState.ACTIVE).in(routeDao);
        Route r3 = new GivenRoute().withDriver(driver).withVehicle(vehicle).withRouteState(RouteState.COMPLETE).in(routeDao);

        // Act
        Collection<Route> routes = routeDao.getByState_ForDisplay(RouteState.CREATED);

        // Assert
        Assert.assertNotNull(routes);
        Assert.assertThat(routes, hasSize(1));
        Assert.assertThat(routes, hasItem(r1));
        Assert.assertThat(routes, not(hasItem(r2)));
        Assert.assertThat(routes, not(hasItem(r3)));
    }

    @Test
    public void testGetByDriver_ForDisplay_OnlySome() throws Exception {
        // Arrange
        User u1 = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Test").withPassword("lul").in(userDao);
        Driver d1 = new GivenDriver().withUser(u1).in(driverDao);
        User u2 = new GivenUser().withName("Beff", "Beffsen").withUsername("AndenTest").withPassword("lul").in(userDao);
        Driver d2 = new GivenDriver().withUser(u2).in(driverDao);

        Vehicle v1 = new GivenVehicle()
            .withMake("Ford")
            .withModel("Lort")
            .withVintage(1999)
            .withVin(new Vin("d"))
            .in(vehicleDao);

        Vehicle v2 = new GivenVehicle()
            .withMake("Ford")
            .withModel("Lort")
            .withVintage(1999)
            .withVin(new Vin("b"))
            .in(vehicleDao);

        Route r1 = new GivenRoute().withDriver(d1).withVehicle(v1).in(routeDao);
        Route r2 = new GivenRoute().withDriver(d2).withVehicle(v2).in(routeDao);
        Route r3 = new GivenRoute().withDriver(d2).withVehicle(v2).in(routeDao);

        // Act
        Collection<Route> routes = routeDao.getByDriver_ForDisplay(d2);

        // Assert
        Assert.assertNotNull(routes);
        Assert.assertThat(routes, hasSize(2));
        Assert.assertThat(routes, not(hasItem(r1)));
        Assert.assertThat(routes, hasItem(r2));
        Assert.assertThat(routes, hasItem(r3));
    }

    @Test
    public void testGetByDriverAndState_ForDisplay_OnlyOne() throws Exception {
        // Arrange
        User u1 = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Test").withPassword("lul").in(userDao);
        Driver d1 = new GivenDriver().withUser(u1).in(driverDao);
        User u2 = new GivenUser().withName("Beff", "Beffsen").withUsername("AndenTest").withPassword("lul").in(userDao);
        Driver d2 = new GivenDriver().withUser(u2).in(driverDao);

        Vehicle v1 = new GivenVehicle()
            .withMake("Ford")
            .withModel("Lort")
            .withVintage(1999)
            .withVin(new Vin("d"))
            .in(vehicleDao);

        Vehicle v2 = new GivenVehicle()
            .withMake("Ford")
            .withModel("Lort")
            .withVintage(1999)
            .withVin(new Vin("c"))
            .in(vehicleDao);

        Route r1 = new GivenRoute().withDriver(d1).withVehicle(v1).withRouteState(RouteState.CREATED).in(routeDao);
        Route r2 = new GivenRoute().withDriver(d2).withVehicle(v2).withRouteState(RouteState.ACTIVE).in(routeDao);
        Route r3 = new GivenRoute().withDriver(d2).withVehicle(v2).withRouteState(RouteState.COMPLETE).in(routeDao);

        // Act
        Collection<Route> routes = routeDao.getByDriverAndState_ForDisplay(RouteState.CREATED, d1);

        // Assert
        Assert.assertNotNull(routes);
        Assert.assertThat(routes, hasSize(1));
        Assert.assertThat(routes, hasItem(r1));
        Assert.assertThat(routes, not(hasItem(r2)));
        Assert.assertThat(routes, not(hasItem(r3)));
    }

    @Test
    public void testGetByDriverAndState_ForDisplay_OnlySome() throws Exception {
        // Arrange
        User u1 = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Test").withPassword("lul").in(userDao);
        Driver d1 = new GivenDriver().withUser(u1).in(driverDao);
        User u2 = new GivenUser().withName("Beff", "Beffsen").withUsername("AndenTest").withPassword("lul").in(userDao);
        Driver d2 = new GivenDriver().withUser(u2).in(driverDao);

        Vehicle v1 = new GivenVehicle()
            .withMake("Ford")
            .withModel("Lort")
            .withVintage(1999)
            .withVin(new Vin("d"))
            .in(vehicleDao);

        Vehicle v2 = new GivenVehicle()
            .withMake("Ford")
            .withModel("Lort")
            .withVintage(1999)
            .withVin(new Vin("b"))
            .in(vehicleDao);

        Route r1 = new GivenRoute().withDriver(d1).withVehicle(v1).withRouteState(RouteState.CREATED).in(routeDao);
        Route r2 = new GivenRoute().withDriver(d2).withVehicle(v2).withRouteState(RouteState.COMPLETE).in(routeDao);
        Route r3 = new GivenRoute().withDriver(d2).withVehicle(v2).withRouteState(RouteState.COMPLETE).in(routeDao);

        // Act
        Collection<Route> routes = routeDao.getByDriverAndState_ForDisplay(RouteState.COMPLETE, d2);

        // Assert
        Assert.assertNotNull(routes);
        Assert.assertThat(routes, hasSize(2));
        Assert.assertThat(routes, not(hasItem(r1)));
        Assert.assertThat(routes, hasItem(r2));
        Assert.assertThat(routes, hasItem(r3));
    }

    @Test
    public void testGetByDriverAndState_ForDisplay_NoResults() throws Exception {
        // Arrange
        User u1 = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Test").withPassword("lul").in(userDao);
        Driver d1 = new GivenDriver().withUser(u1).in(driverDao);

        Vehicle v1 = new GivenVehicle()
            .withMake("Ford")
            .withModel("Lort")
            .withVintage(1999)
            .withVin(new Vin("d"))
            .in(vehicleDao);

        Route r1 = new GivenRoute().withDriver(d1).withVehicle(v1).withRouteState(RouteState.CREATED).in(routeDao);

        // Act
        Collection<Route> routes = routeDao.getByDriverAndState_ForDisplay(RouteState.ACTIVE, d1);

        // Assert
        Assert.assertNotNull(routes);
        Assert.assertThat(routes, hasSize(0));
        Assert.assertThat(routes, not(hasItem(r1)));
    }

    @Test
    public void testById_Exists() throws Exception {
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
        Route found = routeDao.byId(route.getId());

        // Assert
        Assert.assertNotNull(route);
        Assert.assertEquals(route, found);
    }

    @Test
    public void testById_NotExists() throws Exception {
        // Arrange

        // Act
        Route found = routeDao.byId(-1);

        // Assert
        Assert.assertNull(found);
    }

    @Test
    public void testById_ForDisplay_Exists() throws Exception {
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
        Route found = routeDao.byId_ForDisplay(route.getId());

        // Assert
        Assert.assertNotNull(route);
        Assert.assertEquals(route, found);
    }

    @Test
    public void testById_ForDisplay_NotExists() throws Exception {
        // Arrange

        // Act
        Route found = routeDao.byId_ForDisplay(-1);

        // Assert
        Assert.assertNull(found);
    }
}
