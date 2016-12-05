package rocks.stalin.sw708e16.server.persistence.testdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.*;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.persistence.*;
import rocks.stalin.sw708e16.server.persistence.given.*;
import rocks.stalin.sw708e16.server.spring.datainserter.DevelopmentData;
import rocks.stalin.sw708e16.server.spring.datainserter.DevelopmentDataInserter;

import java.util.Date;

@Service
@Transactional
@DevelopmentData
public class Data extends DevelopmentDataInserter {
    @Autowired
    UserDao userDao;

    @Autowired
    PermissionDao permissionDao;

    @Autowired
    AuthDao authDao;

    @Autowired
    VehicleDao vehicleDao;

    @Autowired
    DriverDao driverDao;

    @Autowired
    RouteDao routeDao;

    @Autowired
    private WaypointDao waypointDao;

    @Override
    public void insert() {
        User jeff = new GivenUser()
                .withName("Jeff", "Jeffsen")
                .withUsername("Jeff")
                .withPassword("fuck")
                .in(userDao);
        AuthToken jeffToken = new GivenAuthToken().forUser(jeff).withToken("testtoken").in(authDao);
        new GivenPermission().forUser(jeff).ofType(PermissionType.User).in(permissionDao);
        new GivenPermission().forUser(jeff).ofType(PermissionType.SuperUser).in(permissionDao);
        Vehicle jeffCar = new GivenVehicle()
            .withMake("Ford")
            .withModel("Escort")
            .withVin(new Vin("ads"))
            .withVintage(1992)
            .in(vehicleDao);

        Driver jeffD = new GivenDriver().withUser(jeff).in(driverDao);

        Route route = new GivenRoute().withDriver(jeffD).withVehicle(jeffCar).in(routeDao);

        User jeffy = new GivenUser()
            .withName("Jeffy", "Jeffysen")
            .withUsername("Jeffy")
            .withPassword("fuck")
            .in(userDao);
        new GivenPermission().forUser(jeffy).ofType(PermissionType.User).in(permissionDao);
        new GivenPermission().forUser(jeffy).ofType(PermissionType.SuperUser).in(permissionDao);
        Driver jeffyD = new GivenDriver().withUser(jeffy).in(driverDao);
        Vehicle jeffyCar = new GivenVehicle()
            .withMake("Coorporate")
            .withModel("Escort")
            .withVin(new Vin("aids"))
            .withVintage(1999)
            .in(vehicleDao);

        new GivenWaypoint().withRoute(route).withLatitude(56.0).withLongitude(10.0).withTimestamp(new Date(1)).in(waypointDao);
        new GivenWaypoint().withRoute(route).withLatitude(56.1).withLongitude(10.1).withTimestamp(new Date(2)).in(waypointDao);
        new GivenWaypoint().withRoute(route).withLatitude(56.2).withLongitude(10.2).withTimestamp(new Date(3)).in(waypointDao);
        new GivenWaypoint().withRoute(route).withLatitude(56.3).withLongitude(10.3).withTimestamp(new Date(4)).in(waypointDao);

        Route r2 = new GivenRoute().withDriver(jeffyD).withVehicle(jeffyCar).in(routeDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.0).withLongitude(11.0).withTimestamp(new Date(1)).in(waypointDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.1).withLongitude(11.1).withTimestamp(new Date(2)).in(waypointDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.2).withLongitude(11.2).withTimestamp(new Date(3)).in(waypointDao);
        new GivenWaypoint().withRoute(r2).withLatitude(57.3).withLongitude(11.3).withTimestamp(new Date(4)).in(waypointDao);
        r2.setRouteState(RouteState.COMPLETE);


    }
}
