package rocks.stalin.sw708e16.server.services;

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
import rocks.stalin.sw708e16.server.core.vehicledata.VehicleData;
import rocks.stalin.sw708e16.server.core.vehicledata.VehicleDataPoint;
import rocks.stalin.sw708e16.server.core.vehicledata.concretedatapoints.CurrentSpeedDataPoint;
import rocks.stalin.sw708e16.server.core.vehicledata.concretedatapoints.FuelLevelDataPoint;
import rocks.stalin.sw708e16.server.persistence.*;
import rocks.stalin.sw708e16.server.persistence.given.*;
import rocks.stalin.sw708e16.server.services.builders.VehicleDataBuilder;

import javax.ws.rs.BadRequestException;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-config.xml"})
@Transactional
public class TestVehicleDataPointService {
    @Autowired
    private VehicleDataService vehicleDataService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RouteDao routeDao;

    @Autowired
    private VehicleDao vehicleDao;

    @Autowired
    private DriverDao driverDao;

    @Autowired
    private VehicleDataDao vehicleDataDao;

    @Test
    public void testAddVehicleData_InsertOne() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        vehicleDataService.setRoute(route);

        VehicleDataPoint averageSpeedDataPoint = new CurrentSpeedDataPoint(1.1);
        VehicleDataPoint averageRpmDataPoint = new FuelLevelDataPoint(1999.0);
        VehicleDataBuilder builder = new VehicleDataBuilder();
        builder.addVehicleDataPoint(averageRpmDataPoint);
        builder.addVehicleDataPoint(averageSpeedDataPoint);
        builder.setTimestamp(new Date(2));

        // Act
        VehicleData made = vehicleDataService.addVehicleData(builder);

        // Assert
        assertThat(made, notNullValue());
        assertThat(made.getTimestamp(), is(builder.getTimestamp()));
        assertThat(made.getVehicleDataPoints(), everyItem(is(notNullValue(VehicleDataPoint.class))));
        assertThat(made.getVehicleDataPoints(), everyItem(hasProperty("vehicleData", is(made))));
        assertThat(made.getVehicleDataPoints(), hasItem(averageRpmDataPoint));
        assertThat(made.getVehicleDataPoints(), hasItem(averageSpeedDataPoint));
    }

    @Test(expected = BadRequestException.class)
    public void testAddVehicleData_BuilderWithoutData() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        vehicleDataService.setRoute(route);

        // Act
        VehicleData made = vehicleDataService.addVehicleData(new VehicleDataBuilder());

        // Assert
    }

    @Test(expected = BadRequestException.class)
    public void testAddVehicleData_BuilderInvalidData_MissingDateAndPoints() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        vehicleDataService.setRoute(route);

        VehicleDataBuilder vehicleDataBuilder = new VehicleDataBuilder();

        // Act
        VehicleData made = vehicleDataService.addVehicleData(vehicleDataBuilder);

        // Assert
    }


    @Test(expected = BadRequestException.class)
    public void testAddVehicleData_BuilderInvalidData_NoPoints() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        vehicleDataService.setRoute(route);

        VehicleDataBuilder vehicleDataBuilder = new VehicleDataBuilder();
        vehicleDataBuilder.setTimestamp(new Date(1));

        // Act
        VehicleData made = vehicleDataService.addVehicleData(vehicleDataBuilder);

        // Assert
    }

    @Test
    public void testGetVehicleData_ForRoute() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        VehicleDataPoint averageSpeedDataPoint = new CurrentSpeedDataPoint(1.1);
        VehicleDataPoint averageRpmDataPoint = new FuelLevelDataPoint(1999.0);
        VehicleData vehicleData = new GivenVehicleData()
            .withTimestamp(new Date(2))
            .withDataPoint(averageRpmDataPoint)
            .withDataPoint(averageSpeedDataPoint)
            .withRoute(route)
            .in(vehicleDataDao);

        // Act
        vehicleDataService.setRoute(route);
        List<VehicleData> made = vehicleDataService.getVehicleData();

        // Assert
        assertThat(made, notNullValue());
        assertThat(made, hasItem(vehicleData));
        assertThat(made, everyItem(notNullValue(VehicleData.class)));
        assertThat(made, everyItem(hasProperty("route", is(route))));
    }
}
