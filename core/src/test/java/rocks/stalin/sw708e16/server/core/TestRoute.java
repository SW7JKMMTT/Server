package rocks.stalin.sw708e16.server.core;

import org.junit.Assert;
import org.junit.Test;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestRoute {
    @Test
    public void testGetLatestWaypoint_NonPresent() {
        // Arrange
        User user = new User("Jens", "Jensen");
        Driver driver = new Driver(user);
        Vehicle vehicle = new Vehicle("Volvo", "V80", 2016, new Vin("ABC123"));
        Route route = new Route(new ArrayList<>(), driver, vehicle);

        // Act
        List<Waypoint> found = route.getLastWaypoints(1);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertTrue(found.isEmpty());
    }

    @Test
    public void testGetLatestWaypoint_OnlyOne() {
        // Arrange
        User user = new User("Jens", "Jensen");
        Driver driver = new Driver(user);
        Vehicle vehicle = new Vehicle("Volvo", "V80", 2016, new Vin("ABC123"));
        Route route = new Route(new ArrayList<>(), driver, vehicle);
        Waypoint waypoint = new Waypoint(new Date(1L), 1.1, 1.1, route);
        route.addWaypoint(waypoint);

        // Act
        List<Waypoint> found = route.getLastWaypoints(1);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(found.size(), 1);
        Assert.assertTrue(found.contains(waypoint));
    }

    @Test
    public void testGetLatestWaypoint_TwoInsertedInOrder() {
        // Arrange
        User user = new User("Jens", "Jensen");
        Driver driver = new Driver(user);
        Vehicle vehicle = new Vehicle("Volvo", "V80", 2016, new Vin("ABC123"));
        Route route = new Route(new ArrayList<>(), driver, vehicle);
        Waypoint wp1 = new Waypoint(new Date(1L), 1.1, 1.1, route);
        Waypoint wp2 = new Waypoint(new Date(2L), 1.1, 1.1, route);
        route.addWaypoint(wp1);
        route.addWaypoint(wp2);

        // Act
        List<Waypoint> found = route.getLastWaypoints(1);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(found.size(), 1);
        Assert.assertTrue(found.contains(wp2));
    }

    @Test
    public void testGetLatestWaypoint_TwoInsertedNotInOrder() {
        // Arrange
        User user = new User("Jens", "Jensen");
        Driver driver = new Driver(user);
        Vehicle vehicle = new Vehicle("Volvo", "V80", 2016, new Vin("ABC123"));
        Route route = new Route(new ArrayList<>(), driver, vehicle);
        Waypoint wp2 = new Waypoint(new Date(2L), 1.1, 1.1, route);
        Waypoint wp1 = new Waypoint(new Date(1L), 1.1, 1.1, route);
        route.addWaypoint(wp2);
        route.addWaypoint(wp1);

        // Act
        List<Waypoint> found = route.getLastWaypoints(1);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(found.size(), 1);
        Assert.assertTrue(found.contains(wp2));
    }

    @Test
    public void testGetLatestWaypoint_ThreeInOrder() {
        // Arrange
        User user = new User("Jens", "Jensen");
        Driver driver = new Driver(user);
        Vehicle vehicle = new Vehicle("Volvo", "V80", 2016, new Vin("ABC123"));
        Route route = new Route(new ArrayList<>(), driver, vehicle);
        Waypoint wp1 = new Waypoint(new Date(1L), 1.1, 1.1, route);
        Waypoint wp2 = new Waypoint(new Date(2L), 1.1, 1.1, route);
        Waypoint wp3 = new Waypoint(new Date(3L), 1.1, 1.1, route);
        route.addWaypoint(wp1);
        route.addWaypoint(wp2);
        route.addWaypoint(wp3);

        // Act
        List<Waypoint> found = route.getLastWaypoints(1);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(found.size(), 1);
        Assert.assertTrue(found.contains(wp3));
    }

    @Test
    public void testGetLatestWaypoint_ThreeOutOfOrderGetTwo() {
        // Arrange
        User user = new User("Jens", "Jensen");
        Driver driver = new Driver(user);
        Vehicle vehicle = new Vehicle("Volvo", "V80", 2016, new Vin("ABC123"));
        Route route = new Route(new ArrayList<>(), driver, vehicle);
        Waypoint wp3 = new Waypoint(new Date(3L), 1.3, 1.3, route);
        Waypoint wp1 = new Waypoint(new Date(1L), 1.1, 1.1, route);
        Waypoint wp2 = new Waypoint(new Date(2L), 1.2, 1.2, route);
        route.addWaypoint(wp2);
        route.addWaypoint(wp3);
        route.addWaypoint(wp1);

        // Act
        List<Waypoint> found = route.getLastWaypoints(2);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(found.size(), 2);
        Assert.assertTrue(found.contains(wp2));
        Assert.assertTrue(found.contains(wp3));
    }
}

