package rocks.stalin.sw708e16.server.persistence.testdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.Vin;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.persistence.*;
import rocks.stalin.sw708e16.server.persistence.given.*;
import rocks.stalin.sw708e16.server.spring.datainserter.DevelopmentData;
import rocks.stalin.sw708e16.server.spring.datainserter.DevelopmentDataInserter;

@Service
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
    }
}
