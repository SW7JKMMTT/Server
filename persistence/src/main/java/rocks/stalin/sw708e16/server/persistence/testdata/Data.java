package rocks.stalin.sw708e16.server.persistence.testdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;
import rocks.stalin.sw708e16.server.persistence.AuthDao;
import rocks.stalin.sw708e16.server.persistence.PermissionDao;
import rocks.stalin.sw708e16.server.persistence.UserDao;
import rocks.stalin.sw708e16.server.persistence.given.GivenAuthToken;
import rocks.stalin.sw708e16.server.persistence.given.GivenPermission;
import rocks.stalin.sw708e16.server.persistence.given.GivenUser;
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
    }
}
