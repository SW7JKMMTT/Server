package rocks.stalin.sw708e16.server.services;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.RouteState;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.persistence.DriverDao;
import rocks.stalin.sw708e16.server.persistence.RouteDao;
import rocks.stalin.sw708e16.server.persistence.VehicleDao;
import rocks.stalin.sw708e16.server.services.builders.RouteBuilder;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.Collection;

@Transactional
@Component
@Path("/route")
public class RouteService {
    @Autowired
    private RouteDao routeDao;

    @Autowired
    private DriverDao driverDao;

    @Autowired
    private VehicleDao vehicleDao;

    /**
     * Gets all routes.
     * @return all routes
     */
    @GET
    @Path("/")
    @Produces("application/json")
    public Collection<Route> getAllRoutes(@QueryParam("state") RouteState routeState) {
        if (routeState == null)
            return routeDao.getAll();

        return routeDao.getAll(routeState);
    }

    /**
     * Creates a route, creates a driver for the user if none is present.
     * @param auser the user, in context, for which to create a route.
     * @param routeBuilder a builder for routes, which takes a vehicleId.
     * @return the route created.
     */
    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed({PermissionType.Constants.USER})
    public Route createRoute(@Context User auser, RouteBuilder routeBuilder) {
        if(routeBuilder == null)
            throw new BadRequestException("Invalid Route Given");

        if(routeBuilder.getVehicleid() == null)
            throw new BadRequestException("No Vehicle Id Given");

        if(auser.getDriver() == null) {
            Driver newDriver = new Driver(auser);
            auser.setDriver(newDriver);
            driverDao.add(newDriver);
        }

        Route route = routeBuilder.build(vehicleDao, auser.getDriver());
        routeDao.add(route);
        return route;
    }

    /**
     * Gets a single route by its id.
     * @param id id of the route to get.
     * @return the route with the given id.
     *
     * @HTTP 404 Route not found
     */
    @GET
    @Path("/{rid}/")
    @Produces
    public Route getRouteById(@PathParam("rid") ObjectId id) {
        Route found = routeDao.byId(id);

        if(found == null)
            throw new NotFoundException("Route not found with given id");

        return found;
    }

    // TODO: Delete???
    // TODO: PUT???

    @Autowired
    private WaypointService waypointService;

    /**
     * The waypoint service is served on the path.
     * @param id the id of the route to deliver a waypoint service for.
     * @return the waypoint service.
     *
     * @HTTP 404 Route not found
     */
    @Path("/{rid}/waypoint/")
    public WaypointService getWaypointService(@PathParam("rid") ObjectId id) {
        Route found = routeDao.byId(id);

        if(found == null)
            throw new NotFoundException("Route not found with given id");

        waypointService.setRoute(found);

        return waypointService;
    }
}
