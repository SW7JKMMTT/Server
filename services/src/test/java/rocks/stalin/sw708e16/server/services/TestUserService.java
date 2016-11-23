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
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;
import rocks.stalin.sw708e16.server.persistence.UserDao;
import rocks.stalin.sw708e16.server.persistence.given.GivenUser;
import rocks.stalin.sw708e16.server.services.builders.PermissionBuilder;
import rocks.stalin.sw708e16.server.services.builders.UserBuilder;
import rocks.stalin.sw708e16.test.DatabaseTest;

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
        User bill = new GivenUser().withName("bill", "Jeffsen").withUsername("bill").withPassword("password").in(userDao);
        User john = new GivenUser().withName("john", "Jeffsen").withUsername("john").withPassword("wordpass").in(userDao);
        User lisa = new GivenUser().withName("lisa", "Jeffsen").withUsername("lisa").withPassword("passpass").in(userDao);

        // Act
        Collection<User> allUsers = userService.getAllUsers();

        // Assert
        Assert.assertTrue(allUsers.contains(bill));
        Assert.assertTrue(allUsers.contains(john));
        Assert.assertTrue(allUsers.contains(lisa));
        Assert.assertTrue(allUsers.size() == 3);
    }

    @Test
    public void testInsertUser_AllValid() throws Exception {
        // Arrange
        UserBuilder userBuilder = new UserBuilder("bent", "password", "bent", "Lam");
        userBuilder.addPermission(PermissionType.SuperUser);
        userBuilder.addPermission(PermissionType.User);

        // Act
        User userInserted = userService.insertUser(userBuilder);

        // Assert
        Assert.assertNotNull(userInserted);
        Assert.assertEquals(userBuilder.getUsername(), userInserted.getUsername());
        Assert.assertEquals(userBuilder.getPassword(), userInserted.getPassword());
        // Loop in test ok or not?
        for (PermissionBuilder permissionBuilder : userBuilder.getPermissionBuilders()) {
            Assert.assertTrue(userInserted.hasPermission(permissionBuilder.getPermission()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertUser_InvalidUsername() throws Exception {
        // Arrange
        UserBuilder userBuilder = new UserBuilder(null, "password", "Lasse", "Ligemeget");

        // Act
        userService.insertUser(userBuilder);

        // Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertUser_InvalidPassword() throws Exception {
        // Arrange
        UserBuilder userBuilder = new UserBuilder("Lisa", null, "Lisa", "Lam");

        // Act
        userService.insertUser(userBuilder);

        // Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertUser_InvalidGivenname() throws Exception {
        // Arrange
        UserBuilder userBuilder = new UserBuilder("Lisa", "hunter2", null, "Lam");

        // Act
        userService.insertUser(userBuilder);

        // Assert
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertUser_InvalidSurname() throws Exception {
        // Arrange
        UserBuilder userBuilder = new UserBuilder("Lisa", "hunter2", "Lisa", null);

        // Act
        userService.insertUser(userBuilder);

        // Assert
    }

    @Test
    public void testGetById() throws Exception {
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("jeff").withPassword("password").in(userDao);
        new GivenUser().withName("Jeff", "Jeffsen").withUsername("john").withPassword("password").in(userDao);

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
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("jeff").withPassword("password").in(userDao);
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.setUsername("john");

        User modifiedUser = userService.modifyUser(jeff.getId(), userBuilder);

        Assert.assertEquals(modifiedUser.getUsername(), userBuilder.getUsername());
        Assert.assertEquals(modifiedUser.getPassword(), jeff.getPassword());
        Assert.assertEquals(modifiedUser.getId(), jeff.getId());
    }

    @Test
    public void testModifyUser_SetPassword() throws Exception {
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("jeff").withPassword("password").in(userDao);
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.setPassword("paswurd");

        User modifiedUser = userService.modifyUser(jeff.getId(), userBuilder);

        Assert.assertEquals(modifiedUser.getUsername(), jeff.getUsername());
        Assert.assertEquals(modifiedUser.getPassword(), userBuilder.getPassword());
        Assert.assertEquals(modifiedUser.getId(), jeff.getId());
    }

    @Test(expected = NotFoundException.class)
    public void testModifyUser_UnknownId() throws Exception {
        ObjectId id = new ObjectId();
        UserBuilder userBuilder = new UserBuilder();

        userService.modifyUser(id, userBuilder);
    }

}
