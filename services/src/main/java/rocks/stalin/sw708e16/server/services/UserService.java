package rocks.stalin.sw708e16.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.UserIcon;
import rocks.stalin.sw708e16.server.core.authentication.Permission;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;
import rocks.stalin.sw708e16.server.persistence.PermissionDao;
import rocks.stalin.sw708e16.server.persistence.UserDao;
import rocks.stalin.sw708e16.server.persistence.file.MemoryBackedRoFile;
import rocks.stalin.sw708e16.server.persistence.file.dao.UserIconFileDao;
import rocks.stalin.sw708e16.server.services.builders.PermissionBuilder;
import rocks.stalin.sw708e16.server.services.builders.UserBuilder;
import rocks.stalin.sw708e16.server.services.exceptions.ConflictException;
import rocks.stalin.sw708e16.server.services.interceptor.BASE64;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

@Transactional
@Component
@Path("/user")
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserIconFileDao userIconDao;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * Get all the {@link User users}. Anyone can list the users.
     *
     * @return A collection of the users.
     */
    @GET
    @Path("/")
    @Produces("application/json")
    @RolesAllowed({PermissionType.Constants.USER})
    public Collection<User> getAllUsers() {
        return userDao.getAll_ForDisplay();
    }

    /**
     * Add a new {@link User user}.
     *
     * @param userBuilder The new {@link UserBuilder} to insert
     * @return The {@link User user} that was inserted
     */
    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed({PermissionType.Constants.SUPERUSER})
    public User insertUser(UserBuilder userBuilder) {
        if (userBuilder == null)
            throw new IllegalArgumentException("New user required");

        if (userBuilder.getUsername() == null)
            throw new IllegalArgumentException("No username");
        if (userBuilder.getPassword() == null)
            throw new IllegalArgumentException("No password");
        if (userBuilder.getGivenname() == null)
            throw new IllegalArgumentException("No givenname");
        if (userBuilder.getSurname() == null)
            throw new IllegalArgumentException("No surname");

        User user = userBuilder.buildWithoutPermissions();
        if (userDao.usernameIsTaken(user.getUsername()))
            throw new ConflictException("A user with that username already exists.");

        userDao.add(user);
        // Ensure all users have the "User" permission.
        userBuilder.addPermission(PermissionType.User);

        for (PermissionBuilder permissionBuilder : userBuilder.getPermissions()) {
            Permission permission = permissionBuilder.build(user);
            user.addPermission(permission);
            permissionDao.add(permission);
        }
        return user;
    }

    /**
     * Get user by their id.
     *
     * @param id      The id of the {@link User user} to retrieve
     * @return The retrieved {@link User user}
     *
     * @HTTP 404 User not found
     */
    @GET
    @Path("/{uid}")
    @Produces("application/json")
    @RolesAllowed({PermissionType.Constants.USER})
    public User getUserById(@PathParam("uid") Long id) {
        User user = userDao.byId_ForDisplay(id);

        if (user == null)
            throw new NotFoundException();

        return user;
    }

    /**
     * Modify a user.
     *
     * @param id      The id of the user to modify
     * @param userBuilder The new {@link UserBuilder}
     * @return The modified {@link User user}
     *
     * @HTTP 404 User not found
     */
    @PUT
    @Path("/{uid}")
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed({PermissionType.Constants.SUPERUSER})
    public User modifyUser(@PathParam("uid") Long id, UserBuilder userBuilder) {
        User user = userDao.byId_ForDisplay(id);
        if (user == null)
            throw new NotFoundException();

        if (userBuilder == null)
            throw new IllegalArgumentException("The Userbuilder given was null.");

        userBuilder.merge(user);

        return user;
    }

    /**
     * Get a users icon as png. Anyone can get any users usericon, they are public.
     *
     * @param id The id of the {@link User user}.
     * @return A {@link InputStream stream} of the png image.
     * @throws IOException If the image is unreadable on disk
     *
     * @HTTP 404 User not found or has no icon
     */
    @GET
    @Path("/{uid}/icon")
    @Produces("image/png")
    @RolesAllowed({PermissionType.Constants.USER})
    @BASE64
    public InputStream getUserIcon(@PathParam("uid") Long id) throws IOException {
        User user = userDao.byId_ForIcon(id);
        if (user == null)
            throw new NotFoundException("User not found");

        UserIcon iconHandle = user.getIcon();
        if (iconHandle == null)
            throw new NotFoundException("User has no icon");

        return userIconDao.fromHandle(iconHandle).read();
    }

    /**
     * Set the icon of a user.
     *
     * @param id    The id of the {@link User user}.
     * @param is    The new user icon in a png format.
     * @return The {@link User user} that had its icon modified.
     * @throws IOException If the disk is unwritable.
     *
     * @HTTP 404 User not found
     */
    @PUT
    @Path("/{uid}/icon")
    @Consumes("image/png")
    @Produces("application/json")
    @RolesAllowed({PermissionType.Constants.USER})
    public User setUserIcon(@PathParam("uid") Long id, InputStream is)
            throws IOException
    {
        User user = userDao.byId_ForDisplay(id);
        if (user == null)
            throw new NotFoundException("User not found");

        UserIcon iconHandle = user.getIcon();

        UserIcon fh = userIconDao.newOrUpdate(iconHandle, new MemoryBackedRoFile(is));
        user.setIcon(fh);

        return user;
    }
}
