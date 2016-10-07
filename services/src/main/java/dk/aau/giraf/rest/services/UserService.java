package dk.aau.giraf.rest.services;

import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.core.User;
import dk.aau.giraf.rest.core.UserIcon;
import dk.aau.giraf.rest.core.authentication.AuthToken;
import dk.aau.giraf.rest.core.authentication.PermissionType;
import dk.aau.giraf.rest.persistence.AuthDao;
import dk.aau.giraf.rest.persistence.UserDao;
import dk.aau.giraf.rest.persistence.file.MemoryBackedRoFile;
import dk.aau.giraf.rest.persistence.file.dao.FileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

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
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    @Qualifier("userIcon")
    private FileDao<UserIcon> userIconDao;

    @Autowired
    private AuthDao authDao;

    private Department department;

    /**
     * Set the currently interesting department.
     *
     * @param department The {@link Department department} to retrieve data to
     */
    void setDepartment(Department department) {
        this.department = department;
    }

    /**
     * Get all the {@link User users} in the department. Anyone can list the users.
     *
     * @param context The {@link SecurityContext securitycontext} of the current request
     * @return A collection of the users of the department
     */
    @GET
    @Path("/")
    @Produces("application/json")
    public Collection<User> getAllUsers(@Context SecurityContext context) {
        return userDao.getAll(department);
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
    @RolesAllowed( {PermissionType.Constants.GUARDIAN})
    public User insertUser(@Context User currentUser, User newUser) {
        if(!currentUser.getDepartment().equals(department))
            throw new NotAuthorizedException("Can't add a user to a department other than your own");

        if(newUser == null)
            throw new IllegalArgumentException("New user required");
        newUser.setDepartment(department);
        userDao.add(newUser);
        return newUser;
    }

    /**
     * Get user by their id. The user has to be associated with the current department.
     * A Guardian can retrieve any user of the department they are autheticated with, while
     * a user can only retrieve themselves.
     *
     * @param id      The id of the {@link User user} to retrieve
     * @param context The {@link SecurityContext securitycontext} of the current request
     * @return The retrieved {@link User user}
     */
    @GET
    @Path("/{uid}")
    @Produces("application/json")
    public User getUserById(@PathParam("uid") long id, @Context SecurityContext context) {
        AuthToken token = authDao.byTokenStr(context.getAuthenticationScheme());
        if(token == null)
            throw new NotAuthorizedException("Only authorized users can retrieve detailed userdata");

        User authenticatedUser = token.getUser();

        if(!authenticatedUser.hasPermission(PermissionType.Guardian) && authenticatedUser.getId() != id)
            throw new NotAuthorizedException(
                "Non-Guardians can't retrieve detailed userdata from anyone but themselves");

        if(!authenticatedUser.getDepartment().equals(this.department))
            throw new NotAuthorizedException("Retrieval of detailed userdata from another department is not allowed");

        User user = userDao.byId(this.department, id);

        if(user == null)
            throw new NotFoundException();

        return user;
    }

    /**
     * Modify a user. Only guardians can modify users
     *
     * @param id      The id of the user to modify
     * @param newUser The new {@link User user}
     * @param context The {@link SecurityContext securitycontext} of the current request
     * @return The modified {@link User user}
     */
    @PUT
    @Path("/{uid}")
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed( {PermissionType.Constants.GUARDIAN})
    public User modifyUser(@PathParam("uid") long id, User newUser, @Context SecurityContext context) {
        AuthToken token = authDao.byTokenStr(context.getAuthenticationScheme());
        //We are authorized guardians so we have a token

        if(!token.getUser().getDepartment().equals(department))
            throw new NotAuthorizedException("Modifying userdata of another department is not allowed");

        User user = userDao.byId(department, id);
        if(user == null)
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
    @GET
    @Path("/{uid}/icon")
    @Produces("image/png")
    public InputStream getUserIcon(@PathParam("uid") long id) throws IOException {
        User user = userDao.byId(department, id);
        if(user == null)
            throw new NotFoundException("User not found");

        UserIcon iconHandle = user.getIcon();
        if(iconHandle == null)
            throw new NotFoundException("User has no icon");

        return userIconDao.fromHandle(iconHandle).read();
    }

    /**
     * Set the icon of a user. Only guardians can set icons for users.
     *
     * @param id      The id of the {@link User user}.
     * @param is      The new user icon in a png format.
     * @param auser   The {@link User Authenticated user} of the current request.
     * @return The {@link User user} that had its icon modified.
     * @throws IOException If the disk is unwritable.
     */
    @PUT
    @Path("/{uid}/icon")
    @Consumes("image/png")
    @Produces("application/json")
    @RolesAllowed({PermissionType.Constants.GUARDIAN})
    public User setUserIcon(@PathParam("uid") long id, InputStream is, @Context User auser)
        throws IOException
    {
        //We are authorized guardians so we have a user

        if(!auser.getDepartment().equals(department))
            throw new NotAuthorizedException("Modifying userdata of another department is not allowed");

        User user = userDao.byId(department, id);
        if(user == null)
            throw new NotFoundException("User not found");

        UserIcon iconHandle = user.getIcon();

        UserIcon fh = userIconDao.newOrUpdate(iconHandle, new MemoryBackedRoFile(is));
        user.setIcon(fh);

        return user;
    }
}
