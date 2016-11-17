package rocks.stalin.sw708e16.server.persistence;

import junit.framework.Assert;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.Vin;
import rocks.stalin.sw708e16.server.persistence.given.GivenVehicle;
import rocks.stalin.sw708e16.test.DatabaseTest;

import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dao-config.xml"})
@Transactional
public class TestVehicleDao extends DatabaseTest {
    @Autowired
    private VehicleDao vehicleDao;

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
        Vehicle notfound = vehicleDao.byId(new ObjectId());

        // Assert
        Assert.assertNull(notfound);
    }
    
}
