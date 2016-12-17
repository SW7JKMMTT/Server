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
import rocks.stalin.sw708e16.server.persistence.given.GivenDriver;
import rocks.stalin.sw708e16.server.persistence.given.GivenRoute;
import rocks.stalin.sw708e16.server.persistence.given.GivenUser;
import rocks.stalin.sw708e16.server.persistence.given.GivenVehicle;
import rocks.stalin.sw708e16.test.DatabaseTest;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dao-config.xml"})
@Transactional
public class TestVehicleDao extends DatabaseTest {
    @Autowired
    private VehicleDao vehicleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DriverDao driverDao;

    @Autowired
    private RouteDao routeDao;

    @Test
    public void testGetAll_Empty() throws Exception {
        // Arrange

        // Act
        Collection<Vehicle> allVehicles = vehicleDao.getAll();

        // Assert
        Assert.assertNotNull(allVehicles);
        Assert.assertTrue(allVehicles.isEmpty());
    }

    @Test
    public void testGetAll() throws Exception {
        // Arrange
        Vehicle vehicle = new GivenVehicle()
                .withMake("AAU")
                .withModel("H.O.T.")
                .withVintage(1969)
                .withVin(new Vin("Capri Sonne"))
                .in(vehicleDao);

        // Act
        Collection<Vehicle> allVehicles = vehicleDao.getAll();

        // Assert
        Assert.assertNotNull(allVehicles);
        Assert.assertTrue(allVehicles.size() == 1);
        Assert.assertTrue(allVehicles.contains(vehicle));
    }

    @Test
    public void testById_Found() throws Exception {
        // Arrange
        Vehicle vehicle = new GivenVehicle()
                .withMake("AAU")
                .withModel("H.O.T.")
                .withVintage(1969)
                .withVin(new Vin("Capri Sonne"))
                .in(vehicleDao);

        // Act
        Vehicle found = vehicleDao.byId(vehicle.getId());

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(vehicle, found);
    }

    @Test
    public void testById_NotFound() throws Exception {
        // Arrange

        // Act
        Vehicle notfound = vehicleDao.byId(-1);

        // Assert
        Assert.assertNull(notfound);
    }

    @Test
    public void testByVin_Found() throws Exception {
        // Arrange
        Vehicle vehicle = new GivenVehicle()
                .withMake("AAU")
                .withModel("H.O.T.")
                .withVintage(1969)
                .withVin(new Vin("ABC123"))
                .in(vehicleDao);

        // Act
        Vehicle found = vehicleDao.byVin("ABC123");

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(vehicle, found);
    }

    @Test
    public void testByVin_NotFound() throws Exception {
        // Arrange

        // Act
        Vehicle notfound = vehicleDao.byId(-1);

        // Assert
        Assert.assertNull(notfound);
    }

    @Test
    public void testGetAll_DuplicateIfMoreRoutes() throws Exception {
        // Arrange
        User user = new GivenUser().withName("a", "b").withUsername("a").withPassword("b").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
                .withMake("AAU")
                .withModel("H.O.T.")
                .withVintage(1969)
                .withVin(new Vin("ABC123"))
                .in(vehicleDao);
        new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);

        // Act
        Collection<Vehicle> found = vehicleDao.getAll();

        // Assert
        assertThat(found, notNullValue());
        assertThat(found, hasSize(1));
        assertThat(found, hasItem(vehicle));
    }
}
