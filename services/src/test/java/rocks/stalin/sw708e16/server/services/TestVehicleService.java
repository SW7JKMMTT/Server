package rocks.stalin.sw708e16.server.services;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.VehicleIcon;
import rocks.stalin.sw708e16.server.core.Vin;
import rocks.stalin.sw708e16.server.persistence.VehicleDao;
import rocks.stalin.sw708e16.server.persistence.file.dao.VehicleIconFileDao;
import rocks.stalin.sw708e16.server.persistence.given.GivenVehicle;
import rocks.stalin.sw708e16.server.persistence.given.GivenVehicleIcon;
import rocks.stalin.sw708e16.server.services.builders.VehicleBuilder;
import rocks.stalin.sw708e16.server.services.exceptions.ConflictException;
import rocks.stalin.sw708e16.test.DatabaseTest;

import javax.annotation.Resource;
import javax.ws.rs.NotFoundException;
import java.io.InputStream;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-config.xml"})
@Transactional
public class TestVehicleService extends DatabaseTest {
    @Resource
    private VehicleService vehicleService;

    @Resource
    private VehicleDao vehicleDao;

    @Autowired
    private VehicleIconFileDao vehicleIconFileDao;

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
        vehicleService.getVehicle(new Long(-1));

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

    @Test(expected = NotFoundException.class)
    public void testGetVehicleIcon_IconDoesNotExist() throws Exception {
        // Arrange
        Vehicle vehicle = new GivenVehicle()
            .withMake("AAU")
            .withModel("H.O.T.")
            .withVintage(1969)
            .withVin(new Vin("ABC123"))
            .in(vehicleDao);

        // Act
        vehicleService.getVehicleIcon(vehicle.getId());

        // Assert
    }

    @Test
    public void testGetVehicleIcon_IconDoesExist() throws Exception {
        // Arrange
        VehicleIcon vehicleIcon = new GivenVehicleIcon().withContent("testGetVehicleIcon_IconDoesExist").in(vehicleIconFileDao);

        Vehicle vehicle = new GivenVehicle()
            .withMake("AAU")
            .withModel("H.O.T.")
            .withVintage(1969)
            .withVin(new Vin("ABC123"))
            .withVehicleIcon(vehicleIcon)
            .in(vehicleDao);

        // Act
        InputStream inputStream = vehicleService.getVehicleIcon(vehicle.getId());

        // Assert
        Assert.assertNotNull(inputStream);
        Assert.assertEquals("testGetVehicleIcon_IconDoesExist", IOUtils.toString(inputStream, "UTF-8"));

        // Clean-up ???
    }

    @Test(expected = NotFoundException.class)
    public void testGetVehicleIcon_VehicleNotFound() throws Exception {
        // Arrange

        // Act
        vehicleService.getVehicleIcon(new Long(-1));

        // Assert

    }

    @Test(expected = NotFoundException.class)
    public void testSetOrUpdateVehicleIcon_VehicleNotFound() throws Exception {
        // Arrange

        // Act
        vehicleService.setOrUpdateVehicleIcon(new Long(-1), null);

        // Assert

    }

    @Test
    public void testSetOrUpdateVehicleIcon_IconDoesNotAlreadyExist() throws Exception {
        // Arrange
        InputStream is = IOUtils.toInputStream("testSetOrUpdateVehicleIcon_IconDoesNotAlreadyExist");
        Vehicle vehicle = new GivenVehicle()
            .withMake("AAU")
            .withModel("H.O.T.")
            .withVintage(1969)
            .withVin(new Vin("ABC123"))
            .in(vehicleDao);

        // Act
        Vehicle found = vehicleService.setOrUpdateVehicleIcon(vehicle.getId(), is);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(vehicle, found);
        Assert.assertNotNull(found.getVehicleIcon());

    }

    @Test
    public void testSetOrUpdateVehicleIcon_IconDoesAlreadyExist() throws Exception {
        // Arrange
        InputStream is = IOUtils.toInputStream("testSetOrUpdateVehicleIcon_IconDoesNotAlreadyExist");
        VehicleIcon vehicleIcon = new GivenVehicleIcon().withContent("doesntmatter").in(vehicleIconFileDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("AAU")
            .withModel("H.O.T.")
            .withVintage(1969)
            .withVin(new Vin("ABC123"))
            .withVehicleIcon(vehicleIcon)
            .in(vehicleDao);

        // Act
        Vehicle found = vehicleService.setOrUpdateVehicleIcon(vehicle.getId(), is);

        // Assert
        String filecontents = IOUtils.toString(vehicleIconFileDao.fromHandle(found.getVehicleIcon()).read());
        Assert.assertNotNull(found);
        Assert.assertEquals(vehicle, found);
        Assert.assertNotNull(found.getVehicleIcon());
        Assert.assertNotEquals("doesntmatter", filecontents);
        Assert.assertEquals("testSetOrUpdateVehicleIcon_IconDoesNotAlreadyExist" , filecontents);
    }

    @Test
    public void testModifyVehicle_AllGood() throws Exception {
        // Arrange
        Vehicle vehicle = new GivenVehicle()
            .withMake("AAU")
            .withModel("H.O.T.")
            .withVintage(1969)
            .withVin(new Vin("Capri Sonne"))
            .in(vehicleDao);
        VehicleBuilder builder = new VehicleBuilder().withMake("AU");

        // Act
        Vehicle found = vehicleService.modifyVehicle(vehicle.getId(), builder);

        // Assert
        assertThat(found, notNullValue());
        assertThat(found.getId(), is(vehicle.getId()));
        assertThat(found.getMake(), is(builder.getMake()));
    }
}
