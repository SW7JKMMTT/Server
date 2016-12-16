package rocks.stalin.sw708e16.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.vehicledata.VehicleData;
import rocks.stalin.sw708e16.server.core.vehicledata.VehicleDataPoint;
import rocks.stalin.sw708e16.server.persistence.VehicleDataDao;
import rocks.stalin.sw708e16.server.services.builders.VehicleDataBuilder;

import javax.ws.rs.*;
import java.util.List;

@Transactional
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class VehicleDataService {
    private Route route;

    void setRoute(Route route) {
        this.route = route;
    }

    @Autowired
    private VehicleDataDao vehicleDataDao;

    /**
     * Gets all the {@link VehicleData} for the current {@link Route}.
     * @return The {@link VehicleData} for the current {@link Route}.
     */
    @GET
    @Path("/")
    @Produces("application/json")
    public List<VehicleData> getVehicleData() {
        return vehicleDataDao.byRoute(route);
    }

    /**
     * Adds {@link VehicleData} to the current {@link Route}.
     * @param vehicleData A {@link VehicleDataBuilder} containing at least one {@link VehicleDataPoint}.
     * @return The {@link VehicleData} which was added.
     *
     * @HTTP 400 The {@link VehicleDataBuilder} was malformed or missing.
     */
    @POST
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    public VehicleData addVehicleData(VehicleDataBuilder vehicleData) {
        if (vehicleData == null) {
            throw new BadRequestException("The vehicledata was missing or invalid");
        }

        try {
            VehicleData built = vehicleData.build(route);
            vehicleDataDao.add(built);
            return built;
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException(exception.getMessage(), exception);
        }
    }
}
