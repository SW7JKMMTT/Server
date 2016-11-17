package rocks.stalin.sw708e16.server.persistence;

import org.hamcrest.Matcher;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
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
import rocks.stalin.sw708e16.server.persistence.given.*;
import rocks.stalin.sw708e16.test.DatabaseTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dao-config.xml"})
@Transactional
public class TestWaypointDao extends DatabaseTest {
    @Autowired
    WaypointDao waypointDao;

    @Autowired
    UserDao userDao;

    @Autowired
    RouteDao routeDao;

    @Autowired
    VehicleDao vehicleDao;

    @Autowired
    DriverDao driverDao;

    @PersistenceContext
    EntityManager em;
    FullTextEntityManager ftem;

    @Before
    public void setupFullTextEntityManger() {
        ftem = Search.getFullTextEntityManager(em);
    }

    protected void flushIndex() {
        ftem.flushToIndexes();
    }

    @Test
    public void testWithinRadius_WithTwo_FindTwo() throws Exception {
        User user = new GivenUser().withName("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint way = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(1)
            .withLongitude(1)
            .withTimestamp(new Date())
            .in(waypointDao);
        Waypoint way2 = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(1.5)
            .withLongitude(1)
            .withTimestamp(new Date())
            .in(waypointDao);

        flushIndex();

        List<Waypoint> ret = waypointDao.withinRadius(1, 1, 100);

        Assert.assertThat(ret, hasSize(2));
        Assert.assertThat(ret, hasItem(way));
        Assert.assertThat(ret, hasItem(way2));
    }

    @Test
    public void testWithinRadius_WithTwo_FindOne() throws Exception {
        User user = new GivenUser().withName("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        Waypoint way = new GivenWaypoint()
            .withRoute(route)
            .withLatitude(1)
            .withLongitude(1)
            .withTimestamp(new Date())
            .in(waypointDao);
        new GivenWaypoint()
            .withRoute(route)
            .withLatitude(1.5)
            .withLongitude(1)
            .withTimestamp(new Date())
            .in(waypointDao);

        flushIndex();

        List<Waypoint> ret = waypointDao.withinRadius(1, 1, 40);

        Assert.assertThat(ret, hasSize(1));
        Assert.assertThat(ret, hasItem(way));
    }

    @Test
    public void testWithinRadius_WithTwo_FindNone() throws Exception {
        User user = new GivenUser().withName("Jeff").withPassword("pass").in(userDao);
        Driver driver = new GivenDriver().withUser(user).in(driverDao);
        Vehicle vehicle = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVintage(1920)
            .withVin(new Vin("100"))
            .in(vehicleDao);
        Route route = new GivenRoute().withDriver(driver).withVehicle(vehicle).in(routeDao);
        new GivenWaypoint()
            .withRoute(route)
            .withLatitude(40.71)
            .withLongitude(-74.01)
            .withTimestamp(new Date())
            .in(waypointDao);
        new GivenWaypoint()
            .withRoute(route)
            .withLatitude(57)
            .withLongitude(10)
            .withTimestamp(new Date())
            .in(waypointDao);

        flushIndex();

        List<Waypoint> ret = waypointDao.withinRadius(0, 0, 100);

        Assert.assertThat(ret, hasSize(0));
    }

    @Test
    public void testWithinRadius_WithNone_FindNone() throws Exception {
        List<Waypoint> ret = waypointDao.withinRadius(0, 0, 100);

        Assert.assertThat(ret, hasSize(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithinRadius_InvalidLatitude() throws Exception {
        waypointDao.withinRadius(100, 10, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithinRadius_InvalidLongitude() throws Exception {
        waypointDao.withinRadius(10, 190, 100);
    }
}
