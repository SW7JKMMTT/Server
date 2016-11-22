package rocks.stalin.sw708e16.server.services.builders;

import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.Permission;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A {@link UserBuilder} is used to build new {@link User users}.
 */
public class UserBuilder {
    private String username;
    private String password;
    private Collection<PermissionBuilder> permissionBuilders = new ArrayList<>();

    public UserBuilder() {
    }

    public UserBuilder(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * A required (unique) username, to build a new user.
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * A required password (no complexity requirements), to build a new user.
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * An optional collection of {@link PermissionBuilder PermissionBuilders}.
     */
    public Collection<PermissionBuilder> getPermissionBuilders() {
        return this.permissionBuilders;
    }

    public void addPermission(PermissionType permission) {
        if (permission != null && !permissionBuilders.stream().anyMatch((perm) -> perm.getPermission().equals(permission)))
            this.permissionBuilders.add(new PermissionBuilder(permission));
    }

    /**
     * Builds a {@link User} if all the required fields are set.
     *
     * @return The {@link User} constructed.
     */
    public User buildWithoutPermissions() {
        if (username == null || password == null) {
            throw new IllegalArgumentException("User specified without Username and/or Password.");
        }

        return new User(username, password);
    }

    /**
     * Applies all fields on the {@link UserBuilder} onto the {@link User}.
     *
     * @param user A {@link User} to merge with.
     * @return the merged {@link User}.
     */
    public User merge(User user) {
        if (user == null)
            throw new IllegalArgumentException("User to merge with was null.");

        if (this.username != null)
            user.setUsername(this.username);

        if (this.password != null)
            user.setPassword(this.password);

        for (PermissionBuilder permissionBuilder : permissionBuilders) {
            if (!user.hasPermission(permissionBuilder.getPermission()))
                user.addPermission(new Permission(user, permissionBuilder.getPermission()));
        }

        return user;
    }
}
