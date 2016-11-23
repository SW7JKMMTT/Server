package rocks.stalin.sw708e16.server.services.builders;

import org.junit.Assert;
import org.junit.Test;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.Permission;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;

public class TestUserBuilder {
    @Test
    public void testMerge_NoNewInfo() throws Exception {
        // Arrange
        User user = new UserBuilder("Lasse", "hunter2", "Lasse", "Lam").buildWithoutPermissions();
        user.addPermission(new Permission(user, PermissionType.SuperUser));
        UserBuilder userBuilder = new UserBuilder();

        // Act
        User found = userBuilder.merge(user);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals(user, found);
    }

    @Test
    public void testMerge_NewUsername() throws Exception {
        // Arrange
        User user = new UserBuilder("Lasse", "hunter2", "Lasse", "Lam").buildWithoutPermissions();
        user.addPermission(new Permission(user, PermissionType.SuperUser));
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.setUsername("Maria");

        // Act
        User found = userBuilder.merge(user);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals("Maria", found.getUsername());
        Assert.assertEquals("hunter2", found.getPassword());
    }

    @Test
    public void testMerge_NullFields() throws Exception {
        // Arrange
        User user = new UserBuilder("Lasse", "hunter2", "Lasse", "Lam").buildWithoutPermissions();
        user.addPermission(new Permission(user, PermissionType.SuperUser));
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.setUsername(null);

        // Act
        User found = userBuilder.merge(user);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertEquals("Info should not have changed.", "Lasse", found.getUsername());
        Assert.assertEquals("hunter2", found.getPassword());
    }

    @Test
    public void testMerge_NewPermissions() throws Exception {
        // Arrange
        User user = new UserBuilder("Lasse", "hunter2", "Lasse", "Lam").buildWithoutPermissions();
        user.addPermission(new Permission(user, PermissionType.SuperUser));
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.addPermission(PermissionType.User);

        // Act
        User found = userBuilder.merge(user);

        // Assert
        Assert.assertNotNull(found);
        Assert.assertTrue(user.hasPermission(PermissionType.User));
        Assert.assertTrue(user.hasPermission(PermissionType.SuperUser));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMerge_Null() throws Exception {
        // Arrange
        UserBuilder userBuilder = new UserBuilder("Lasse", "hunter2", "Lasse", "Lam");
        userBuilder.addPermission(PermissionType.User);

        // Act
        userBuilder.merge(null);

        // Assert
    }
}
