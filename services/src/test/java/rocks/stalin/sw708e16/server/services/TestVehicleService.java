package rocks.stalin.sw708e16.server.services;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.Vin;
import rocks.stalin.sw708e16.server.persistence.VehicleDao;
import rocks.stalin.sw708e16.server.persistence.given.GivenVehicle;
import rocks.stalin.sw708e16.server.services.builders.VehicleBuilder;
import rocks.stalin.sw708e16.server.services.exceptions.ConflictException;
import rocks.stalin.sw708e16.test.DatabaseTest;

import javax.annotation.Resource;
import javax.ws.rs.NotFoundException;

import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-config.xml"})
@Transactional
public class TestVehicleService extends DatabaseTest {
    @Resource
    private VehicleService vehicleService;

    @Resource
    private VehicleDao vehicleDao;

    @Test
    public void testGetAllVehicles_WithNone() throws Exception {
        // Arrange

        // Act
        Collection<Vehicle> allVehicles = vehicleService.getAllVehicles();

        // Assert
        Assert.assertNotNull(allVehicles);
        Assert.assertTrue(allVehicles.isEmpty());
    }

    @Test
    public void testGetAllVehicles_WithOne() throws Exception {
        // Arrange
        Vehicle vehicle = new GivenVehicle()
                .withMake("AAU")
                .withModel("H.O.T.")
                .withVintage(1969)
                .withVin(new Vin("Capri Sonne"))
                .in(vehicleDao);

        // Act
        Collection<Vehicle> allVehicles = vehicleService.getAllVehicles();

        // Assert
        Assert.assertNotNull(allVehicles);
        Assert.assertEquals(1, allVehicles.size());
        Assert.assertTrue(allVehicles.contains(vehicle));
    }

    @Test
    public void testGetAllVehicle_WithMany() throws Exception {
        // Arrange
        Vehicle v1 = new GivenVehicle()
                .withMake("AAU")
                .withModel("H.O.T.")
                .withVintage(1969)
                .withVin(new Vin("Capri Sonne"))
                .in(vehicleDao);
        Vehicle v2  = new GivenVehicle()
                .withMake("AAU")
                .withModel("H.O.T. 50")
                .withVintage(1980)
                .withVin(new Vin("ABC123"))
                .in(vehicleDao);

        // Act
        Collection<Vehicle> allVehicles = vehicleService.getAllVehicles();

        // Assert
        Assert.assertNotNull(allVehicles);
        Assert.assertEquals(2, allVehicles.size());
        Assert.assertTrue(allVehicles.contains(v1));
        Assert.assertTrue(allVehicles.contains(v2));
    }

    @Test(expected = NotFoundException.class)
    public void testGetVehicle_NotFound() throws Exception {
        // Arrange

        // Act
        vehicleService.getVehicle(new ObjectId());

        // Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVehicle_InvalidObjectId() throws Exception {
        // Arrange

        // Act
        vehicleService.getVehicle(null);

        // Assert
    }



    @Test
    public void testGetVehicle_GetOne() throws Exception {
        // Arrange
        Vehicle vehicle = new GivenVehicle()
                .withMake("AAU")
                .withModel("H.O.T.")
                .withVintage(1969)
                .withVin(new Vin("Capri Sonne"))
                .in(vehicleDao);

        // Act
        Vehicle found = vehicleService.getVehicle(vehicle.getId());

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(vehicle, found);
    }

    @Test
    public void testAddVehicle_AllValid() throws Exception {
        // Arrange
        VehicleBuilder vehicleBuilder = new VehicleBuilder()
                .withMake("Ford")
                .withModel("Mustang")
                .withVintage(1966)
                .withVin(new Vin("ACB312"));

        // Act
        Vehicle found = vehicleService.addVehicle(vehicleBuilder);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(vehicleBuilder.getMake(), found.getMake());
        Assert.assertEquals(vehicleBuilder.getModel(), found.getModel());
        Assert.assertEquals(vehicleBuilder.getVin(), found.getVin());
        Assert.assertEquals(vehicleBuilder.getVintage().intValue(), found.getVintage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVehicle_MissingVin() throws Exception {
        // Arrange
        VehicleBuilder vehicleBuilder = new VehicleBuilder()
                .withMake("Ford")
                .withModel("Mustang")
                .withVintage(1966);

        // Act
        Vehicle found = vehicleService.addVehicle(vehicleBuilder);

        // Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVehicle_VintageNull() throws Exception {
        // Arrange
        VehicleBuilder vehicleBuilder = new VehicleBuilder()
                .withMake("Ford")
                .withModel("Mustang")
                .withVintage(null)
                .withVin(new Vin("ABC123"));

        // Act
        Vehicle found = vehicleService.addVehicle(vehicleBuilder);

        // Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVehicle_VehicleBuilderNull() throws Exception {
        // Arrange

        // Act
        Vehicle found = vehicleService.addVehicle(null);

        // Assert
    }

    @Test(expected = ConflictException.class)
    public void testAddVehicle_SameVin() throws Exception {
        // Arrange
        Vehicle vehicle = new GivenVehicle()
                .withMake("AAU")
                .withModel("H.O.T.")
                .withVintage(1969)
                .withVin(new Vin("ABC123"))
                .in(vehicleDao);
        VehicleBuilder vehicleBuilder = new VehicleBuilder()
                .withMake("Ford")
                .withModel("Mustang")
                .withVintage(1966)
                .withVin(new Vin("ABC123"));

        // Act
        Vehicle found = vehicleService.addVehicle(vehicleBuilder);

        // Assert
    }

}
