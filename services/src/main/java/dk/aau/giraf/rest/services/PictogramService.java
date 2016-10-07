package dk.aau.giraf.rest.services;

import dk.aau.giraf.rest.core.*;
import dk.aau.giraf.rest.core.authentication.AuthToken;
import dk.aau.giraf.rest.core.authentication.PermissionType;
import dk.aau.giraf.rest.persistence.AuthDao;
import dk.aau.giraf.rest.persistence.DepartmentDao;
import dk.aau.giraf.rest.persistence.PictogramDao;
import dk.aau.giraf.rest.persistence.UserDao;
import dk.aau.giraf.rest.persistence.file.MemoryBackedRoFile;
import dk.aau.giraf.rest.persistence.file.dao.FileDao;
import dk.aau.giraf.rest.services.builder.PictogramBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Role;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Protected/Private pictogram service
 *
 * <p>
 *     Used to get information and images from protected/pricate pictograms. That is, pictograms owned by any person or
 *     department.
 *     Any guardion for a department should be allowed to create and edit these pictograms, with only some users from
 *     the department allowed to read/view them.
 * </p>
 */
@Transactional
public class PictogramService {
    @Autowired
    private PictogramDao pictogramDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    @Qualifier("pictogramImage")
    private FileDao<PictogramImage> pictogramImageFileDao;

    private Department department;

    /**
     * Set the currently interesting department.
     * @param department The {@link Department department} to retrieve data for
     */
    void setDepartment(Department department) {
        this.department = department;
    }

    /**
     * Get a list of all the private/protected {@link Pictogram pictograms}. Any user of a department can do this.
     * @param user The currently authenticated user
     * @return A collection of {@link Pictogram pictograms}.
     */
    @GET
    @Path("/")
    @Produces("application/json")
    @RolesAllowed({ PermissionType.Constants.USER })
    public Collection<Pictogram> getAllPictograms(@Context User user) {
        if(user == null)
            throw new NotAuthorizedException("You need to be logged in to add department pictograms");
        if(!user.getDepartment().equals(department) && !user.hasPermission(PermissionType.SuperUser))
            throw new NotAuthorizedException("You are logged in to the wrong department");

        return pictogramDao.getAll(user);
    }

    // TODO: Implement a search (Should be done in the JAVA EE general way with reflection 'n' shit).

    /**
     * Get a specific private/protected pictogram. Anyone allowed to read the pictogram can do this.
     * @param user The currently authenticated user.
     * @param id Of the pictogram to read.
     * @return The {@link Pictogram pictogram} object.
     */
    @GET
    @Path("/{pid}")
    @Produces("application/json")
    @RolesAllowed({ PermissionType.Constants.USER })
    public Pictogram getPictogram(@Context User user, @PathParam("pid") long id) {
        if(user == null)
            throw new NotAuthorizedException("You need to be logged in to add department pictograms");
        if(!user.getDepartment().equals(department) && !user.hasPermission(PermissionType.SuperUser))
            throw new NotAuthorizedException("You are logged in to the wrong department");

        Pictogram pictogram = pictogramDao.byId(id);

        if (pictogram == null)
            throw new NotFoundException("Pictogram not found");
        if(!pictogram.hasPermission(user))
            throw new NotFoundException("You don't have access to view the selected pictogram");

        return pictogram;
    }

    /**
     * Get the image of a protected/private pictogram. Anyone allowed to read the pictogram can do this
     * @param user The currently authenticated user
     * @param id Of the {@link Pictogram pictogram}.
     * @return The pictogram image in a image/png format.
     * @throws IOException If the image can not be read from disk.
     */
    @GET
    @Path("/{pid}/image")
    @Produces("image/png")
    @RolesAllowed({ PermissionType.Constants.USER })
    public InputStream getPictogramImage(@Context User user, @PathParam("pid") long id) throws IOException {
        if(user == null)
            throw new NotAuthorizedException("You need to be logged in to add department pictograms");
        if(!user.getDepartment().equals(department) && !user.hasPermission(PermissionType.SuperUser))
            throw new NotAuthorizedException("You are logged in to the wrong department");

        Pictogram pictogram = pictogramDao.byId(id);
        if (pictogram == null)
            throw new NotFoundException("Pictogram not found");
        if(!pictogram.hasPermission(user))
            throw new NotFoundException("You don't have access to view the selected pictogram");

        return pictogramImageFileDao.fromHandle(pictogram.getPictogramImage()).read();
    }

    /**
     * Create a new private/protected {@link Pictogram pictogram}. only superusers and guardians can do this.
     * @param user The currently authenticated user.
     * @param pictogramBuilder The information for the new pictogram
     * @return The new {@link Pictogram pictogram} object.
     */
    @POST
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    @RolesAllowed({PermissionType.Constants.GUARDIAN, PermissionType.Constants.SUPERUSER })
    public Pictogram createPictogram(@Context User user, PictogramBuilder pictogramBuilder)
        throws javassist.NotFoundException
    {
        if(user == null)
            throw new NotAuthorizedException("You need to be logged in to add department pictograms");
        if(!user.getDepartment().equals(department) && !user.hasPermission(PermissionType.SuperUser))
            throw new NotAuthorizedException("You are logged in to the wrong department");

        Pictogram pictogram = pictogramBuilder.build(userDao, departmentDao);

        if(pictogram.getDepartment().equals(department))
            throw new NotAuthorizedException("You can't add pictograms to other departments");
        if(pictogram.getAccessLevel() == AccessLevel.PUBLIC)
            throw new NotAllowedException("Users cannot make public pictograms");

        if(!pictogram.getOwner().getDepartment().equals(department))
            throw new NotAuthorizedException("You can't assign pictograms to users not in your department");
        pictogramDao.add(pictogram);

        return pictogram;
    }

    /**
     * Upload an image to a {@link Pictogram pictogram}. Only superusers and guardians can do this.
     * @param user The authenticated user.
     * @param id If the {@link Pictogram pictogram} to upload an image to.
     * @param is The image in a png format.
     * @return The new {@link Pictogram pictogram} object.
     * @throws IOException If the file can't be written to disc.
     */
    @PUT
    @Path("/{pid}/image")
    @Consumes("image/png")
    @Produces("application/json")
    @RolesAllowed({ PermissionType.Constants.GUARDIAN, PermissionType.Constants.SUPERUSER })
    public Pictogram uploadPictogramImage(@Context User user, @PathParam("pid") long id, InputStream is)
        throws IOException
    {
        if(user == null)
            throw new NotAuthorizedException("You need to be logged in to add department pictograms");
        if(!user.getDepartment().equals(department) && !user.hasPermission(PermissionType.SuperUser))
            throw new NotAuthorizedException("You are logged in to the wrong department");

        Pictogram pictogram = pictogramDao.byId(id);
        if (pictogram == null)
            throw new NotFoundException("Pictogram not found");
        if (!pictogram.hasPermission(user))
            throw new NotFoundException("No permission");

        PictogramImage pictogramImageHandle = pictogram.getPictogramImage();

        PictogramImage fh = pictogramImageFileDao.newOrUpdate(pictogramImageHandle, new MemoryBackedRoFile(is));
        pictogram.setPictogramImage(fh);
        return pictogram;
    }

    /**
     * Update a private/protected pictogram {@link Pictogram pictogram} with some new data. Only superusers and guardians
     * can do this.
     * @param user The currently authenticated user.
     * @param id of the {@link Pictogram pictogram} to update.
     * @param pictogram filled with the data to update the {@link Pictogram pictogram} with.
     * @return The new {@link Pictogram pictogram}.
     */
    @PUT
    @Path("/{pid}")
    @Consumes("application/json")
    @Produces("application/json")
    @RolesAllowed({ PermissionType.Constants.GUARDIAN, PermissionType.Constants.SUPERUSER })
    public Pictogram updatePictogram(@Context User user, @PathParam("pid") long id, Pictogram pictogram) {
        if(user == null)
            throw new NotAuthorizedException("You need to be logged in to add department pictograms");
        if(!user.getDepartment().equals(department) && !user.hasPermission(PermissionType.SuperUser))
            throw new NotAuthorizedException("You are logged in to the wrong department");

        Pictogram oldPictogram = pictogramDao.byId(id);
        if (oldPictogram == null)
            throw new NotFoundException("Pictogram to update not found");
        if (oldPictogram.getAccessLevel() == AccessLevel.PUBLIC)
            throw new NotAllowedException("Public pictograms cannot be updated");

        if (!oldPictogram.hasPermission(user))
            throw new NotFoundException("No permission");

        oldPictogram.merge(pictogram);
        return oldPictogram;
    }

}
