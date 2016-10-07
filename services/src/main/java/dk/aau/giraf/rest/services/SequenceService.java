package dk.aau.giraf.rest.services;

import dk.aau.giraf.rest.core.*;
import dk.aau.giraf.rest.core.authentication.PermissionType;
import dk.aau.giraf.rest.persistence.DepartmentDao;
import dk.aau.giraf.rest.persistence.FrameDao;
import dk.aau.giraf.rest.persistence.SequenceDao;
import dk.aau.giraf.rest.persistence.UserDao;
import dk.aau.giraf.rest.services.builder.SequenceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Transactional
public class SequenceService {

    @Autowired
    private SequenceDao sequenceDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private FrameDao frameDao;

    private Department department;

    /**
     * Set the department.
     *
     * @param department to be set
     */
    public void setDepartment(Department department) {
        this.department = department;
    }

    /**
     * Get {@link Sequence sequences} of the department.
     *
     * @param user the {@link User user} which makes the request
     * @param title optional query string to search by title
     * @return a collection of all found {@link Sequence sequences}
     * @throws NoContentException is user is unauthorized
     */
    @GET
    @Path("/")
    @Produces("application/json")
    @RolesAllowed(PermissionType.Constants.USER)
    public Collection<Sequence> getDepartmentSequences(@Context User user, @QueryParam("title") String title)
        throws NoContentException
    {

        if (!(user.getDepartment().equals(department)))
            throw new NotFoundException("User is not allowed in the Department");

        if (title != null && title.length() > 0)
            return sequenceDao.searchByUserAndTitle(user, title);

        return sequenceDao.byDepartment(department);
    }

    /**
     * Get specific {@link Sequence sequence}.
     *
     * @param id the id of the {@link Sequence sequence}
     * @param user the {@link User user} which makes the request
     * @return the {@link Sequence sequence} if found
     * @throws NoContentException is user is unauthorized
     */
    @GET
    @Path("/{sid}")
    @Produces("application/json")
    public Sequence getSequence(@PathParam("sid") long id, @Context User user)
        throws NoContentException
    {
        if (!(user != null && user.getDepartment().equals(department)))
            throw new NotFoundException("User is not allowed in Department");

        Sequence sequence = sequenceDao.byId(id);
        if (!sequence.hasPermission(user))
            throw new NoContentException("Not found");

        return sequence;
    }

    /**
     * Create a new {@link Sequence sequence}.
     * Only users with {@link PermissionType PermissionType} SuperUser and Guardian can do this.
     *
     * @param user The currently authenticated user.
     * @param sequenceBuilder The information for the new sequence
     * @return The new {@link Sequence sequence} object.
     */
    @POST
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    @RolesAllowed( {PermissionType.Constants.GUARDIAN, PermissionType.Constants.SUPERUSER})
    public Sequence createSequence(@Context User user, SequenceBuilder sequenceBuilder) {
        if (!user.getDepartment().equals(department))
            throw new NotAuthorizedException("User has to be part of the department");

        Sequence sequence = sequenceBuilder.build(userDao, departmentDao, frameDao);

        if (sequence.getAccessLevel() == AccessLevel.PUBLIC && !user.hasPermission(PermissionType.SuperUser))
            throw new NotAllowedException("Only SuperUsers can create public Sequences");

        if (!sequence.hasPermission(user) || !sequence.hasPermission(sequence.getOwner()))
            throw new NotAuthorizedException("You cannot assign a sequence to users not in your department");

        sequenceDao.add(sequence);

        return sequence;
    }


    /**
     * Update the meta data of a {@link Sequence sequence}.
     *
     * @param user the user trying to make the update
     * @param id the id of the {@link Sequence sequence}
     * @param sequenceBuilder The information to be merged into the original
     * @return the updated {@link Sequence sequence}
     * @throws NoContentException is user is unauthorized
     */
    @PUT
    @Path("/{sid}")
    @Produces("application/json")
    @RolesAllowed( {PermissionType.Constants.GUARDIAN, PermissionType.Constants.SUPERUSER})
    public Sequence updateSequence(@Context User user, @PathParam("sid") long id, SequenceBuilder sequenceBuilder)
        throws NotFoundException
    {
        if (!(user.getDepartment().equals(department)))
            throw new NotFoundException("User is not allowed in Department");

        Sequence sequence = sequenceDao.byId(id);

        if (sequence == null)
            throw new NotFoundException("The Sequence was not found");

        if (!sequence.hasPermission(user))
            throw new NotFoundException("The User did not have permission");

        if (sequence.getOwner() != null && !sequence.hasPermission(user))
            throw new NotAuthorizedException("The User cannot create a Sequence it does not have access to");

        if (sequence.getAccessLevel() == AccessLevel.PUBLIC &&
            !user.hasPermission(PermissionType.SuperUser))
        {
            throw new NotAllowedException("You are not allowed to make public sequences");
        }

        sequenceBuilder.merge(userDao, departmentDao, frameDao, sequence);

        return sequence;
    }

    /**
     * Delete a given {@link Sequence sequence}.
     *
     * @param user the user trying to delete the sequence
     * @param id the id of the {@link Sequence sequence}
     * @return true if the delete was successful
     * @throws NoContentException is user is unauthorized
     */
    @DELETE
    @Path("/{sid}")
    @Produces("application/json")
    @RolesAllowed( {PermissionType.Constants.GUARDIAN, PermissionType.Constants.SUPERUSER})
    public Response deleteSequence(@Context User user, @PathParam("sid") long id)
        throws NoContentException
    {
        if (!(user.getDepartment().equals(department)))
            throw new NotFoundException("User not allowed in deparment");

        Sequence sequence = sequenceDao.byId(id);

        if (sequence == null)
            throw new NotFoundException("The Sequence was not found");

        if (!sequence.hasPermission(user))
            throw new NotFoundException("The user did not have access to the sequence");

        if (sequence.getAccessLevel() == AccessLevel.PUBLIC)
            throw new NotAllowedException("You cannot delete this");

        sequenceDao.remove(sequence);
        return Response.ok().build();
    }
}
