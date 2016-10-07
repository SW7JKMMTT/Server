package dk.aau.giraf.rest.services;

import dk.aau.giraf.rest.core.*;
import dk.aau.giraf.rest.core.authentication.AuthToken;
import dk.aau.giraf.rest.core.authentication.Permission;
import dk.aau.giraf.rest.core.authentication.PermissionType;
import dk.aau.giraf.rest.persistence.AuthDao;
import dk.aau.giraf.rest.persistence.DepartmentDao;
import dk.aau.giraf.rest.persistence.PictogramDao;
import dk.aau.giraf.rest.persistence.UserDao;
import dk.aau.giraf.rest.persistence.file.MemoryBackedRoFile;
import dk.aau.giraf.rest.persistence.file.dao.FileDao;
import dk.aau.giraf.rest.services.builder.PictogramBuilder;
import javassist.*;
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
 * Public pictogram service
 *
 * <p>
 *     Used to get information and images from public pictograms. That is, pictograms not owned by any person or
 *     department.
 *     Only superusers should be allowed to edit these pictograms, but anyone can read them
 * </p>
 */
@Transactional
@Path("/pictogram")
public class PublicPictogramService {
    @Autowired
    private PictogramDao pictogramDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    @Qualifier("pictogramImage")
    private FileDao<PictogramImage> pictogramImageFileDao;

    /**
     * Get a list of all the public {@link Pictogram pictograms}. Anyone can do this.
     * @return A collection of public {@link Pictogram pictograms}.
     */
    @GET
    @Path("/")
    @Produces("application/json")
    public Collection<Pictogram> getAllPictograms() {
        return pictogramDao.getAllPublicPictograms();
    }

    // TODO: Implement a search (Should be done in the JAVA EE general way with reflection 'n' shit).

    /**
     * Get a specific public pictogram. Anyone can do this.
     * @param id Of the pictogram to read.
     * @return The {@link Pictogram pictogram} object.
     */
    @GET
    @Path("/{pid}")
    @Produces("application/json")
    public Pictogram getPictogram(@PathParam("pid") long id) {
        Pictogram pictogram = pictogramDao.byId(id);

        if (pictogram == null)
            throw new NotFoundException("Pictogram not found");

        if (pictogram.getAccessLevel() != AccessLevel.PUBLIC)
            throw new NotFoundException("Pictogram not found");

        return pictogram;
    }

    /**
     * Get the image of a public pictogram. Anyone can do this.
     * @param id Of the {@link Pictogram pictogram}.
     * @return The pictogram image in a image/png format.
     * @throws IOException If the image can not be read from disk.
     */
    @GET
    @Path("/{pid}/image")
    @Produces("image/png")
    public InputStream getPictogramImage(@PathParam("pid") long id) throws IOException {
        Pictogram pictogram = pictogramDao.byId(id);

        if (pictogram == null)
            throw new NotFoundException("Pictogram not found");

        if (pictogram.getAccessLevel() != AccessLevel.PUBLIC)
            throw new NotFoundException("Pictogram not found");

        if(pictogram.getPictogramImage() == null)
            throw new NotFoundException("Pictogram has no image. This is not good");

        return pictogramImageFileDao.fromHandle(pictogram.getPictogramImage()).read();
    }

    /**
     * Create a new public {@link Pictogram pictogram}. only superusers can do this.
     * @param pictogramBuilder The information for the new pictogram
     * @return The new {@link Pictogram pictogram} object.
     */
    @POST
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    @RolesAllowed({ PermissionType.Constants.SUPERUSER })
    public Pictogram createPictogram(PictogramBuilder pictogramBuilder) {
        Pictogram pictogram = null;
        //TODO: Fix this. It should not require a user
        try {
            pictogram = pictogramBuilder.build(userDao, departmentDao);
        } catch (javassist.NotFoundException e) {
            e.printStackTrace();
        }
        pictogramDao.add(pictogram);
        return pictogram;
    }

    /**
     * Upload an image to a {@link Pictogram pictogram}. Only superusers can do this.
     * @param id If the {@link Pictogram pictogram} to upload an image to.
     * @param is The image in a png format.
     * @return The new {@link Pictogram pictogram} object.
     * @throws IOException If the file can't be written to disc.
     */
    @PUT
    @Path("/{pid}/image")
    @Consumes("image/png")
    @Produces("application/json")
    @RolesAllowed({ PermissionType.Constants.SUPERUSER })
    public Pictogram uploadPictogramImage(@PathParam("pid") long id, InputStream is) throws IOException {
        Pictogram pictogram = pictogramDao.byId(id);

        if (pictogram == null)
            throw new NotFoundException("Pictogram to update not found");

        PictogramImage pictogramImageHandle = pictogram.getPictogramImage();

        if(pictogramImageHandle != null)
            pictogramImageFileDao.remove(pictogramImageHandle);

        PictogramImage fh = pictogramImageFileDao.newOrUpdate(pictogramImageHandle, new MemoryBackedRoFile(is));
        pictogram.setPictogramImage(fh);
        return pictogram;
    }

    /**
     * Update a public pictogram {@link Pictogram pictogram} with some new data. Only superusers can update public
     * pictograms.
     * @param id of the {@link Pictogram pictogram} to update.
     * @param pictogram filled with the data to update the {@link Pictogram pictogram} with.
     * @return The new {@link Pictogram pictogram}.
     */
    @PUT
    @Path("/{pid}")
    @Consumes("application/json")
    @Produces("application/json")
    @RolesAllowed({ PermissionType.Constants.SUPERUSER })
    public Pictogram updatePictogram(@PathParam("pid") long id, Pictogram pictogram) {
        Pictogram oldPictogram = pictogramDao.byId(id);

        if (oldPictogram == null)
            throw new NotFoundException("Pictogram to update not found");

        oldPictogram.merge(pictogram);
        return oldPictogram;
    }

    /**
     * Delete a public {@link Pictogram pictogram}. Only superusers can delete public pictograms.
     * @param id of the {@link Pictogram pictogram} to delete.
     */
    @DELETE
    @Path("/{pid}")
    @RolesAllowed({ PermissionType.Constants.SUPERUSER })
    public void deletePictogram(@PathParam("pid") long id) {
        Pictogram pictogram = pictogramDao.byId(id);

        if (pictogram == null)
            throw new NotFoundException("Pictogram to update not found");
        if(pictogram.getPictogramImage() == null)
            throw new NotFoundException("No pictogram image to delete");

        pictogramImageFileDao.remove(pictogram.getPictogramImage());
        pictogramDao.remove(pictogram);
    }
}
