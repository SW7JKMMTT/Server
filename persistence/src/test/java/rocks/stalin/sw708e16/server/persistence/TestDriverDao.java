package rocks.stalin.sw708e16.server.persistence;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.persistence.given.GivenDriver;
import rocks.stalin.sw708e16.server.persistence.given.GivenUser;
import rocks.stalin.sw708e16.test.DatabaseTest;

import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dao-config.xml"})
@Transactional
public class TestDriverDao extends DatabaseTest {
    @Autowired
    private DriverDao driverDao;

    @Autowired
    private UserDao userDao;

    @Test
    public void testGetAll_Empty() throws Exception {
        // Arrange

        // Act
        Collection<Driver> allDriver = driverDao.getAll();

        // Assert
        Assert.assertNotNull(allDriver);
        Assert.assertTrue(allDriver.isEmpty());
    }

    @Test
    public void testGetAll() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Test").withPassword("lul").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);

        // Act
        Collection<Driver> allDrivers = driverDao.getAll();

        // Assert
        Assert.assertNotNull(allDrivers);
        Assert.assertTrue(allDrivers.size() == 1);
        Assert.assertTrue(allDrivers.contains(driver));
    }

    @Test
    public void testById_Found() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Test").withPassword("lul").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);

        // Act
        Driver found = driverDao.byId(driver.getId());

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(driver, found);
    }

    @Test
    public void testByid_NotFound() throws Exception {
        // Arrange

        // Act
        Driver notfound = driverDao.byId(new ObjectId());

        // Assert
        Assert.assertNull(notfound);
    }
}
