package rocks.stalin.sw708e16.server.services;

import org.junit.Assert;
import org.junit.Before;
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
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;
import rocks.stalin.sw708e16.server.persistence.*;
import rocks.stalin.sw708e16.server.persistence.given.*;
import rocks.stalin.sw708e16.server.services.builders.WaypointBuilder;
import rocks.stalin.sw708e16.test.DatabaseTest;

import java.util.Collection;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-config.xml"})
@Transactional
public class TestWaypointService extends DatabaseTest {
    @Autowired
    private WaypointService waypointService;

    @Autowired
    private RouteDao routeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DriverDao driverDao;

    @Autowired
    private VehicleDao vehicleDao;

    @Autowired
    private WaypointDao waypointDao;

    /**
     * Creates a route which will be used in all unit tests.
     * Is deleted and created before each test.
     */
    @Before
    public void SetupWaypointService() {
        User user = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Anders").withPassword("hunter2").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
                .withMake("Ford")
                .withModel("Mondeo")
                .withVintage(1999)
                .withVin(new Vin("ABC123"))
                .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        waypointService.setRoute(route);
    }


    @Test
    public void getAllWaypointsInRoute_Empty() throws Exception {
        // Arrange

        // Act
        Collection<Waypoint> allWaypoints = waypointService.getWaypoints(0);

        // Assert
        Assert.assertNotNull(allWaypoints);
        Assert.assertTrue(allWaypoints.isEmpty());
    }

    @Test
    public void getAllWaypointsInRoute_WithOne() throws Exception {
        // Arrange
        Waypoint waypoint = new GivenWaypoint()
                .withRoute(waypointService.getRoute())
                .withLatitude(1.0)
                .withLatitude(1.1)
                .withTimestamp(new Date(1L))
                .in(waypointDao);

        // Act
        Collection<Waypoint> allWaypoints = waypointService.getWaypoints(0);

        // Assert
        Assert.assertNotNull(allWaypoints);
        Assert.assertFalse(allWaypoints.isEmpty());
        Assert.assertEquals(allWaypoints.size(), 1);
        Assert.assertTrue(allWaypoints.contains(waypoint));
    }

    @Test
    public void getAllWaypointsInRoute_WithTwoAndMaxOne() throws Exception {
        // Arrange
        Waypoint wp1 = new GivenWaypoint()
                .withRoute(waypointService.getRoute())
                .withLatitude(1.0)
                .withLatitude(1.1)
                .withTimestamp(new Date(2L))
                .in(waypointDao);
        new GivenWaypoint()
                .withRoute(waypointService.getRoute())
                .withLatitude(1.2)
                .withLatitude(1.3)
                .withTimestamp(new Date(1L))
                .in(waypointDao);

        // Act
        Collection<Waypoint> allWaypoints = waypointService.getWaypoints(1);

        // Assert
        Assert.assertNotNull(allWaypoints);
        Assert.assertFalse(allWaypoints.isEmpty());
        Assert.assertEquals(allWaypoints.size(), 1);
        Assert.assertTrue(allWaypoints.contains(wp1));
    }

    @Test
    public void getAllWaypointsInRoute_WithTwoAndMaxTwo() throws Exception {
        // Arrange
        Waypoint wp1 = new GivenWaypoint()
                .withRoute(waypointService.getRoute())
                .withLatitude(1.0)
                .withLatitude(1.1)
                .withTimestamp(new Date(2L))
                .in(waypointDao);
        Waypoint wp2 = new GivenWaypoint()
                .withRoute(waypointService.getRoute())
                .withLatitude(1.2)
                .withLatitude(1.3)
                .withTimestamp(new Date(1L))
                .in(waypointDao);

        // Act
        Collection<Waypoint> allWaypoints = waypointService.getWaypoints(2);

        // Assert
        Assert.assertNotNull(allWaypoints);
        Assert.assertFalse(allWaypoints.isEmpty());
        Assert.assertEquals(allWaypoints.size(), 2);
        Assert.assertTrue(allWaypoints.contains(wp1));
        Assert.assertTrue(allWaypoints.contains(wp2));
    }


    @Test
    public void addWaypoint_ValidData() throws Exception {
        // Arrange
        WaypointBuilder waypoint = new WaypointBuilder();
        waypoint.setLatitude(1.1);
        waypoint.setLongitude(1.1);
        waypoint.setTimestamp(new Date(1L));

        //Act
        Waypoint found = waypointService.addWaypoint(waypoint);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(found.getLatitude(), waypoint.getLatitude(), 0.001);
        Assert.assertEquals(found.getLongitude(), waypoint.getLongitude(), 0.001);
        Assert.assertEquals(found.getTimestamp(), waypoint.getTimestamp());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWaypoint_InvalidData() throws Exception {
        // Arrange
        WaypointBuilder waypoint = new WaypointBuilder();
        waypoint.setLatitude(1.1);
        waypoint.setTimestamp(new Date(1L));
        // Missing parameter(s).

        // Act
        Waypoint found = waypointService.addWaypoint(waypoint);

        // Assert
    }
}
