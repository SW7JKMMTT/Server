package rocks.stalin.sw708e16.server.services;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.VehicleIcon;
import rocks.stalin.sw708e16.server.persistence.VehicleDao;
import rocks.stalin.sw708e16.server.persistence.file.MemoryBackedRoFile;
import rocks.stalin.sw708e16.server.persistence.file.dao.FileDao;
import rocks.stalin.sw708e16.server.persistence.file.dao.VehicleIconFileDao;
import rocks.stalin.sw708e16.server.services.builders.VehicleBuilder;
import rocks.stalin.sw708e16.server.services.exceptions.ConflictException;

import javax.annotation.Resource;
import javax.ws.rs.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

@Transactional
@Component
@Path("/vehicle")
public class VehicleService {
    @Resource
    private VehicleDao vehicleDao;

    @Autowired
    @Qualifier("vehicleIcon")
    private VehicleIconFileDao vehicleIconDao;

    /**
     * Gets all {@link Vehicle}s.
     *
     * @return All {@link Vehicle}s in the database.
     */
    @Path("/")
    @GET
    @Produces("application/json")
    public Collection<Vehicle> getAllVehicles() {
        return vehicleDao.getAll();
    }

    /**
     * Gets the {@link Vehicle} with the given {@link ObjectId}.
     *
     * @param id The {@link ObjectId} to find the {@link Vehicle} for.
     * @return The {@link Vehicle} found.
     *
     * @HTTP 404 Vehicle not found
     */
    @Path("/{vid}")
    @GET
    @Produces("application/json")
    public Vehicle getVehicle(@PathParam("vid") ObjectId id) {
        if (id == null)
            throw new IllegalArgumentException("Given id was null.");

        Vehicle vehicle = vehicleDao.byId(id);

        if (vehicle == null)
            throw new NotFoundException("No vehicle with the given id was found.");

        return vehicleDao.byId(id);
    }

    /**
     * Adds a {@link Vehicle} to the database if a valid {@link VehicleBuilder} is given.
     *
     * @param vehicleBuilder A {@link VehicleBuilder} to construct a {@link Vehicle} from.
     * @return The {@link Vehicle} constructed.
     *
     * @HTTP 409 A vehicle with that Vin already exists.
     */
    @Path("/")
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Vehicle addVehicle(VehicleBuilder vehicleBuilder) {
        if (vehicleBuilder == null) {
            throw new IllegalArgumentException("No Vehicle given.");
        }

        // Test to see if Vin is already in database.
        if (vehicleBuilder.getVin() != null && vehicleDao.byVin(vehicleBuilder.getVin()) != null) {
            throw new ConflictException("A vehicle with that Vin already exists.");
        }

        Vehicle vehicle = vehicleBuilder.build();
        vehicleDao.add(vehicle);

        return vehicle;
    }



    /**
     * Gets the {@link VehicleIcon} as a PNG.
     *
     * @param id the id of the {@link Vehicle} to get the image of.
     * @return The PNG icon.
     * @throws IOException Disk error.
     *
     * @HTTP 404 if Vehicle was not found or it has no icon.
     */
    @GET
    @Path("/{vid}/icon")
    @Produces("image/png")
    public InputStream getVehicleIcon(@PathParam("vid") ObjectId id) throws IOException {
        Vehicle vehicle = vehicleDao.byId(id);
        if (vehicle == null)
            throw new NotFoundException("Vehicle not found.");

        VehicleIcon vehicleIconHandle = vehicle.getVehicleIcon();
        if (vehicleIconHandle == null)
            throw new NotFoundException("Vehicle has no icon.");

        return vehicleIconDao.fromHandle(vehicleIconHandle).read();
    }

    /**
     * Sets or updates the {@link VehicleIcon icon} of a {@link Vehicle}.
     *
     * @param id the id of the {@link Vehicle} to change the icon of.
     * @param is the icon to set.
     * @return the {@link Vehicle} which is changed.
     * @throws IOException Disk error.
     *
     * @HTTP 404 if Vehicle was not found.
     */
    @PUT
    @Path("/{vid}/icon")
    @Consumes("image/png")
    @Produces("application/json")
    public Vehicle setOrUpdateVehicleIcon(@PathParam("vid") ObjectId id, InputStream is) throws IOException {
        Vehicle vehicle = vehicleDao.byId(id);
        if (vehicle == null)
            throw new NotFoundException("Vehicle not found.");

        VehicleIcon vehicleIconHandle = vehicle.getVehicleIcon();

        VehicleIcon fh = vehicleIconDao.newOrUpdate(vehicleIconHandle, new MemoryBackedRoFile(is));
        vehicle.setVehicleIcon(fh);

        return vehicle;
    }
}
