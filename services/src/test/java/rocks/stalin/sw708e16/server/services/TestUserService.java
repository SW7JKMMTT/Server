package rocks.stalin.sw708e16.server.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.persistence.UserDao;
import rocks.stalin.sw708e16.test.DatabaseTest;
import rocks.stalin.sw708e16.test.given.GivenUser;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
// Load the beans to configure, here the DAOs
@ContextConfiguration(locations = {"classpath:test-config.xml"})
// apply the transaction manager to the test class so every DAO methods are executed
// within a transaction
@Transactional
public class TestUserService extends DatabaseTest {
    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Test
    public void TestGetAll() throws Exception {
        // Arrange
        User superuser = new GivenUser().withName("superuser").withPassword("password").in(userDao);
        User u1 = new GivenUser().withName("testuser").withPassword("password").in(userDao);
        User u2 = new GivenUser().withName("anothertestuser").withPassword("password").in(userDao);

        // Act
        Collection<User> userlist = userService.getAllUsers();

        // Assert
        Assert.assertTrue(userlist.contains(superuser));
        Assert.assertTrue(userlist.contains(u1));
        Assert.assertTrue(userlist.contains(u2));
    }

    @Test
    public void TestInsertUser() throws Exception {
        // Arrange
        User superuser = new GivenUser().withName("superuser").withPassword("password").in(userDao);
        User u1 = new User("testuser", "password");

        // Act
        User userInserted = userService.insertUser(superuser, u1);

        // Assert
        Assert.assertEquals(u1, userInserted);
        Assert.assertEquals(userDao.byUsername(u1.getUsername()), userInserted);
    }

    @Test
    public void TestGetById() throws Exception {
        User u1 = new GivenUser().withName("u1").withPassword("password").in(userDao);
        new GivenUser().withName("u2").withPassword("password").in(userDao);

        User gottenUser = userService.getUserById(u1.getId());

        Assert.assertEquals(u1, gottenUser);
    }

}
