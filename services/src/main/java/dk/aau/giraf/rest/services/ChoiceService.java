package dk.aau.giraf.rest.services;

import dk.aau.giraf.rest.core.*;
import dk.aau.giraf.rest.core.authentication.PermissionType;
import dk.aau.giraf.rest.persistence.FrameDao;
import dk.aau.giraf.rest.services.builder.ChoiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import java.util.Iterator;

@Transactional
public class ChoiceService {

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
     * Get specific {@link Choice choice}.
     *
     * @param id   the id of the {@link Choice choice}
     * @param user the {@link User user} which makes the request
     * @return the {@link Choice choice} if found
     * @throws NoContentException is user is unauthorized
     */
    @GET
    @Path("/{cid}")
    @Produces("application/json")
    public Choice getChoice(@PathParam("cid") long id, @Context User user)
        throws NoContentException
    {
        if (!(user != null && user.getDepartment().equals(department)))
            throw new NotFoundException("User is not allowed in Department");

        Choice choice = (Choice) frameDao.byId(id);

        Iterator<PictoFrame> options = choice.iterator();
        while (options.hasNext()) {
            if (!options.next().hasPermission(user))
                throw new NotAllowedException("User does not have permission to at least one of the options");
        }

        return choice;
    }

    /**
     * Create a new {@link Choice choice}.
     * Only users with {@link PermissionType PermissionType} SuperUser and Guardian can do this.
     *
     * @param user            The currently authenticated user.
     * @param choiceBuilder The information for the new choice
     * @return The new {@link Choice choice} object.
     */
    @POST
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    @RolesAllowed( {PermissionType.Constants.GUARDIAN, PermissionType.Constants.SUPERUSER})
    public Choice createChoice(@Context User user, ChoiceBuilder choiceBuilder) {
        if (user == null || !user.getDepartment().equals(department))
            throw new NotAuthorizedException("User unauthorized");

        Choice choice = choiceBuilder.build(frameDao);

        Iterator<PictoFrame> options = choice.iterator();
        while (options.hasNext()) {
            if (!options.next().hasPermission(user))
                throw new NotAllowedException("User does not have permission to at least one of the options");
        }

        frameDao.add(choice);

        return choice;
    }


    /**
     * Update the meta data of a {@link Choice choice}.
     *
     * @param user            the user trying to make the update
     * @param id              the id of the {@link Choice choice}
     * @return the updated {@link Choice choice}
     * @throws NotFoundException is user is unauthorized
     */
    @PUT
    @Path("/{cid}")
    @Produces("application/json")
    @RolesAllowed( {PermissionType.Constants.GUARDIAN, PermissionType.Constants.SUPERUSER})
    public Choice updateChoice(@Context User user, @PathParam("cid") long id, ChoiceBuilder choiceBuilder)
        throws NotFoundException
    {
        Choice choice = (Choice) frameDao.byId(id);

        choiceBuilder.merge(frameDao, choice);

        return choice;
    }

    /**
     * Delete a given {@link Choice choice}.
     *
     * @param user the user trying to delete the choice
     * @param id   the id of the {@link Choice choice}
     * @return true if the delete was successful
     * @throws NoContentException is user is unauthorized
     */
    @DELETE
    @Path("/{cid}")
    @Produces("application/json")
    @RolesAllowed({PermissionType.Constants.GUARDIAN, PermissionType.Constants.SUPERUSER })
    public Response deleteSequence(@Context User user, @PathParam("cid") long id)
        throws NoContentException
    {
        try {
            Choice choice = (Choice) frameDao.byId(id);
            frameDao.remove(choice);
        } catch (Exception e) {
            return Response.noContent().build();
        }

        return Response.ok().build();
    }
}
