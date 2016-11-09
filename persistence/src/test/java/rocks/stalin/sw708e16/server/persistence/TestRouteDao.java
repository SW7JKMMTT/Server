package rocks.stalin.sw708e16.server.persistence;

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
import rocks.stalin.sw708e16.server.persistence.given.GivenUser;
import rocks.stalin.sw708e16.test.DatabaseTest;
import rocks.stalin.sw708e16.server.persistence.given.GivenDriver;
import rocks.stalin.sw708e16.server.persistence.given.GivenRoute;
import rocks.stalin.sw708e16.server.persistence.given.GivenVehicle;

import java.util.Collection;

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
        Collection<Route> allRoutes = routeDao.getAll();

        // Assert
        Assert.assertNotNull(allRoutes);
        Assert.assertTrue(allRoutes.isEmpty());
    }

    @Test
    public void testGetAll() throws Exception {
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
        Collection<Route> allRoutes = routeDao.getAll();

        // Assert
        Assert.assertNotNull(allRoutes);
        Assert.assertTrue(allRoutes.size() == 1);
        Assert.assertTrue(allRoutes.contains(route));
    }

    @Test
    public void testById_Exists() throws Exception {
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
        Route found = routeDao.byId(route.getId());

        // Assert
        Assert.assertNotNull(route);
        Assert.assertEquals(route, found);
    }

    @Test
    public void testById_NotExists() throws Exception {
        // Arrange
        ObjectId bogusObjectId = new ObjectId();

        // Act
        Route found = routeDao.byId(bogusObjectId);

        // Assert
        Assert.assertNull(found);
    }
}
