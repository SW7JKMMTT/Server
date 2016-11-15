package rocks.stalin.sw708e16.server.persistence;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
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

    @Test
    public void withinRadius() throws Exception {
        FullTextEntityManager ftem = Search.getFullTextEntityManager(em);
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
        ftem.persist(way);
        new GivenWaypoint()
            .withRoute(route)
            .withLatitude(1)
            .withLongitude(1)
            .withTimestamp(new Date())
            .in(waypointDao);
        ftem.flushToIndexes();
        List<Waypoint> ret = waypointDao.withinRadius(1, 1, 100);
        assertFalse(ret.isEmpty());
    }
}