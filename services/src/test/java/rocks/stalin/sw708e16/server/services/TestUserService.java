package rocks.stalin.sw708e16.server.services;

import org.bson.types.ObjectId;
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
import rocks.stalin.sw708e16.server.persistence.given.GivenUser;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.Collection;

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
    public void testGetAll_Empty() throws Exception {
        // Act
        Collection<User> allUsers = userService.getAllUsers();

        // Assert
        Assert.assertTrue(allUsers.isEmpty());
    }

    @Test
    public void testGetAll() throws Exception {
        // Arrange
        User bill = new GivenUser().withName("bill").withPassword("password").in(userDao);
        User john = new GivenUser().withName("john").withPassword("wordpass").in(userDao);
        User lisa = new GivenUser().withName("lisa").withPassword("passpass").in(userDao);

        // Act
        Collection<User> allUsers = userService.getAllUsers();

        // Assert
        Assert.assertTrue(allUsers.contains(bill));
        Assert.assertTrue(allUsers.contains(john));
        Assert.assertTrue(allUsers.contains(lisa));
        Assert.assertTrue(allUsers.size() == 3);
    }

    @Test
    public void testInsertUser() throws Exception {
        // Arrange
        User bent = new User("bent", "password");

        // Act
        User userInserted = userService.insertUser(bent);

        // Assert
        Assert.assertEquals(bent, userInserted);
        Assert.assertEquals(userDao.byUsername(bent.getUsername()), userInserted);
    }

    @Test(expected = BadRequestException.class)
    public void testInsertUser_NoUsername() throws Exception {
        // Arrange
        User lisa = new User("lisa", "passphrase");
        lisa.setUsername(null); //@HACK: Setting the username to null shouldn't be supported

        // Act
        userService.insertUser(lisa);
    }

    @Test(expected = BadRequestException.class)
    public void testInsertUser_NoPassword() throws Exception {
        // Arrange
        User lisa = new User("lisa", "passphrase");
        lisa.setPassword(null); //@HACK: Setting the password to null shouldn't be supported

        // Act
        userService.insertUser(lisa);
    }

    @Test
    public void testGetById() throws Exception {
        User jeff = new GivenUser().withName("jeff").withPassword("password").in(userDao);
        new GivenUser().withName("john").withPassword("password").in(userDao);

        User gottenUser = userService.getUserById(jeff.getId());

        Assert.assertEquals(jeff, gottenUser);
    }

    @Test(expected = NotFoundException.class)
    public void testGetById_UnknownId() throws Exception {
        ObjectId unknownId = new ObjectId();

        userService.getUserById(unknownId);
    }

    @Test
    public void testModifyUser_SetUsername() throws Exception {
        User jeff = new GivenUser().withName("jeff").withPassword("password").in(userDao);
        User newUser = new User("john", "password");
        newUser.setPassword(null); //@HACK: Setting the password to null should probably be an error

        User modifiedUser = userService.modifyUser(jeff.getId(), newUser);

        Assert.assertEquals(modifiedUser.getUsername(), newUser.getUsername());
        Assert.assertEquals(modifiedUser.getPassword(), jeff.getPassword());
        Assert.assertEquals(modifiedUser.getId(), jeff.getId());
    }

    @Test
    public void testModifyUser_SetPassword() throws Exception {
        User jeff = new GivenUser().withName("jeff").withPassword("password").in(userDao);
        User newUser = new User("john", "password");
        newUser.setUsername(null); //@HACK: Setting the password to null should probably be an error

        User modifiedUser = userService.modifyUser(jeff.getId(), newUser);

        Assert.assertEquals(modifiedUser.getUsername(), jeff.getUsername());
        Assert.assertEquals(modifiedUser.getPassword(), newUser.getPassword());
        Assert.assertEquals(modifiedUser.getId(), jeff.getId());
    }

    @Test(expected = NotFoundException.class)
    public void testModifyUser_UnknownId() throws Exception {
        ObjectId id = new ObjectId();
        User newUser = new User("john", "password");

        userService.modifyUser(id, newUser);
    }

}
