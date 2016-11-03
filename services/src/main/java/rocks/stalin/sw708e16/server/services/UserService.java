package rocks.stalin.sw708e16.server.services;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.UserIcon;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.core.authentication.Permission;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;
import rocks.stalin.sw708e16.server.persistence.AuthDao;
import rocks.stalin.sw708e16.server.persistence.UserDao;
import rocks.stalin.sw708e16.server.persistence.file.MemoryBackedRoFile;
import rocks.stalin.sw708e16.server.persistence.file.dao.FileDao;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

//Most of this class is based on the old pre-user injection method of authorization
//It should be updated to fit new the context injected way of getting users (See pictogramservice.java)
//TODO: Update to context injected users
@Transactional
@Component
@Path("/user")
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    @Qualifier("userIcon")
    private FileDao<UserIcon> userIconDao;

    @Autowired
    private AuthDao authDao;

    /**
     * Get all the {@link User users} in the department. Anyone can list the users.
     *
     * @return A collection of the users of the department
     */
    @GET
    @Path("/")
    @Produces("application/json")
    public Collection<User> getAllUsers() {
        return userDao.getAll();
    }

    /**
     * Add a new {@link User user} to the department. Only guardians can add users.
     *
     * @param currentUser The currently authenticated {@link User user}
     * @param newUser     The new {@link User user}to insert
     * @return The {@link User user} that was inserted
     */
    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed({PermissionType.Constants.SUPERUSER})
    public User insertUser(@Context User currentUser, User newUser) {
        if (newUser == null)
            throw new IllegalArgumentException("New user required");
        userDao.add(newUser);
        return newUser;
    }

    /**
     * Get user by their id.
     *
     * @param id      The id of the {@link User user} to retrieve
     * @param context The {@link SecurityContext securitycontext} of the current request
     * @return The retrieved {@link User user}
     */
    @Deprecated
    @GET
    @Path("/{uid}")
    @Produces("application/json")
    @RolesAllowed({PermissionType.Constants.SUPERUSER})
    public User getUserById(@PathParam("uid") ObjectId id) {
        User user = userDao.byId(id);

        if (user == null)
            throw new NotFoundException();

        return user;
    }

    /**
     * Modify a user.
     *
     * @param id      The id of the user to modify
     * @param newUser The new {@link User user}
     * @param context The {@link SecurityContext securitycontext} of the current request
     * @return The modified {@link User user}
     */
    @Deprecated
    @PUT
    @Path("/{uid}")
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed({PermissionType.Constants.SUPERUSER})
    public User modifyUser(@PathParam("uid") ObjectId id, User newUser, @Context SecurityContext context) {
        AuthToken token = authDao.byTokenStr(context.getAuthenticationScheme());

        User user = userDao.byId(id);
        if (user == null)
            throw new NotFoundException();
        user.merge(newUser);
        return user;
    }

    /**
     * Get a users icon as png. Anyone can get any users usericon, they are public.
     *
     * @param id The id of the {@link User user}.
     * @return A {@link InputStream stream} of the png image.
     * @throws IOException If the image is unreadable on disk
     */
    @Deprecated
    @GET
    @Path("/{uid}/icon")
    @Produces("image/png")
    public InputStream getUserIcon(@PathParam("uid") ObjectId id) throws IOException {
        User user = userDao.byId(id);
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
     * @param auser The {@link User Authenticated user} of the current request.
     * @return The {@link User user} that had its icon modified.
     * @throws IOException If the disk is unwritable.
     */
    @Deprecated
    @PUT
    @Path("/{uid}/icon")
    @Consumes("image/png")
    @Produces("application/json")
    @RolesAllowed({PermissionType.Constants.USER})
    public User setUserIcon(@PathParam("uid") ObjectId id, InputStream is, @Context User auser)
            throws IOException {
        User user = userDao.byId(id);
        if (user == null)
            throw new NotFoundException("User not found");

        UserIcon iconHandle = user.getIcon();

        UserIcon fh = userIconDao.newOrUpdate(iconHandle, new MemoryBackedRoFile(is));
        user.setIcon(fh);

        return user;
    }
}
