package rocks.stalin.sw708e16.server.services;

import org.hibernate.search.spatial.impl.GeometricConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.RouteState;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.persistence.*;
import rocks.stalin.sw708e16.server.services.builders.RouteBuilder;
import rocks.stalin.sw708e16.server.services.exceptions.DriverNotFoundException;
import rocks.stalin.sw708e16.server.services.exceptions.OffThePlanetException;

import javax.annotation.security.RolesAllowed;
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

    @Autowired
    private WaypointDao waypointDao;

    @Autowired
    private WaypointService waypointService;

    @Autowired
    private VehicleDataService vehicleDataService;

    /**
     * Gets {@link Route routes}, depending on query parameters of which all given are considered.
     * A spatial query is done by giving latitude, longitude and radius.
     *
     * @param routeState The state of the routes to return (Optional otherwise all).
     * @param driverId The id of the driver to get routes for (Optional otherwise all).
     * @param latitude A latitude to query for.
     * @param longitude A longitude to query for.
     * @param radius A radius (in km) from the latitude and longitude to query for.
     * @return A list of all the {@link Route routes} which satisfies the parameters.
     *
     * @HTTP 400 One or more query params was given, but their combination was invalid, or, one or more was bad.
     * @HTTP 404 The driver given was not found.
     */
    @GET
    @Path("/")
    @Produces("application/json")
    @RolesAllowed({PermissionType.Constants.USER})
    public Collection<Route> getAllRoutes(
            @QueryParam("state") RouteState routeState,
            @QueryParam("driver") Long driverId,
            @QueryParam("latitude") Double latitude,
            @QueryParam("longitude") Double longitude,
            @QueryParam("radius") Double radius)
    {
        // Spatial queries:
        if (latitude != null || longitude != null || radius != null) {
            try {
                return spatialQuery(new Coordinate(latitude, longitude), radius, driverId, routeState);
            } catch (IllegalArgumentException | OffThePlanetException exception) {
                throw new BadRequestException(exception.getMessage(), exception);
            } catch (DriverNotFoundException exception) {
                throw new NotFoundException(exception.getMessage(), exception);
            }
        }

        // It's set, let's filter by it!
        if (driverId != null) {
            Driver driver = driverDao.byId(driverId);
            if (driver == null)
                throw new NotFoundException("DriverId was given, but driver wasn't found.");

            // It's also set, let's include that in the filter!
            if (routeState != null)
                return routeDao.getByDriverAndState_ForDisplay(routeState, driver);

            return routeDao.getByDriver_ForDisplay(driver);
        }

        // Only this is set, only include it in filter!
        if (routeState != null)
            return routeDao.getByState_ForDisplay(routeState);

        // No queryparams are set, get'em all!
        return routeDao.getAll_ForDisplay();
    }

    /**
     * Does spatial queries, and handles errors.
     *
     * @param routeState The state of the routes to return (Optional otherwise all).
     * @param driverId The id of the driver to get routes for (Optional otherwise all).
     * @param coordinate The coordinate of the center of the query.
     * @param radius A radius from the latitude and longitude to query for.
     * @return The result of the spatial query.
     */
    private Collection<Route> spatialQuery(Coordinate coordinate, Double radius, Long driverId, RouteState routeState)
        throws OffThePlanetException, DriverNotFoundException
    {
        if (coordinate.getLatitude() == null)
            throw new IllegalArgumentException("latitude not set, but other spatial parameters are.");

        if (coordinate.getLongitude() == null)
            throw new IllegalArgumentException("longitude not set, but other spatial parameters are.");

        if (radius == null)
            throw new IllegalArgumentException("radius not set, but other spatial parameters are.");

        if (coordinate.getLatitude() > GeometricConstants.LATITUDE_DEGREE_MAX || coordinate.getLatitude() < GeometricConstants.LATITUDE_DEGREE_MIN) {
            throw new OffThePlanetException(OffThePlanetException.Component.LATITUDE, coordinate.getLatitude());
        }

        if (coordinate.getLongitude() > GeometricConstants.LONGITUDE_DEGREE_MAX || coordinate.getLongitude() < GeometricConstants.LONGITUDE_DEGREE_MIN) {
            throw new OffThePlanetException(OffThePlanetException.Component.LONGITUDE, coordinate.getLongitude());
        }

        if (radius < 0)
            throw new IllegalArgumentException("radius must be positive was: " + radius);

        // If this is set, the retrieve the object and use it.
        if (driverId != null) {
            Driver driver = driverDao.byId(driverId);
            if (driver == null)
                throw new DriverNotFoundException();

            // If this is set use both to filter.
            if (routeState != null)
                return routeDao.withinRadius_ForDisplay(waypointDao, coordinate, radius, driver, routeState);

            return routeDao.withinRadius_ForDisplay(waypointDao, coordinate, radius, driver);
        }

        if (routeState != null)
            return routeDao.withinRadius_ForDisplay(waypointDao, coordinate, radius, routeState);

        return routeDao.withinRadius_ForDisplay(waypointDao, coordinate, radius);
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


        // Not driverid was given assuming that current user should be used.
        if (routeBuilder.getDriverid() == null) {
            // If user doesn't already have a driver, make one for it.
            if(auser.getDriver() == null) {
                Driver newDriver = new Driver(auser);
                auser.setDriver(newDriver);
                driverDao.add(newDriver);
            }

            routeBuilder.setDriverid(auser.getDriver().getId());
        }

        Route route = routeBuilder.build(vehicleDao, driverDao);
        routeDao.add(route);
        return route;
    }

    /**
     * Marks a {@link Route} as {@link RouteState} Complete.
     *
     * @param id {@link Route} to finish.
     * @return The {@link Route} byRoute_after it is finished.
     *
     * @HTTP 404 Route not found.
     */
    @PUT
    @Path("/{rid}/")
    @Produces("application/json")
    @RolesAllowed({PermissionType.Constants.USER})
    public Route modifyRoute(@PathParam("rid") Long id, RouteBuilder routeBuilder) {
        if (id == null)
            throw new IllegalArgumentException("No Id was given.");

        if (routeBuilder == null)
            throw new IllegalArgumentException("The changes given in the RouteBuilder was null.");

        Route found = routeDao.byId_ForDisplay(id);

        if (found == null)
            throw new IllegalArgumentException("Route with given id was not found.");

        found = routeBuilder.merge(vehicleDao, driverDao, found);

        return found;
    }

    // TODO: Delete???

    /**
     * Gets a single route by its id.
     * @param id id of the route to get.
     * @return the route with the given id.
     *
     * @HTTP 404 Route not found
     */
    @GET
    @Path("/{rid}/")
    @Produces("application/json")
    @RolesAllowed({PermissionType.Constants.USER})
    public Route getRouteById(@PathParam("rid") Long id) {
        Route found = routeDao.byId_ForDisplay(id);

        if(found == null)
            throw new NotFoundException("Route not found with given id");

        return found;
    }

    /**
     * The waypoint service is served on the path.
     * @param id the id of the route to deliver a waypoint service for.
     * @return the waypoint service.
     *
     * @HTTP 404 Route not found
     */
    @Path("/{rid}/waypoint/")
    @RolesAllowed({PermissionType.Constants.USER})
    public WaypointService getWaypointService(@PathParam("rid") Long id) {
        Route found = routeDao.byId(id);

        if(found == null)
            throw new NotFoundException("Route not found with given id");

        waypointService.setRoute(found);

        return waypointService;
    }

    /**
     * Gets the {@link VehicleDataService} of a given {@link Route}.
     * @param id the id of the {@link Route} to create a {@link VehicleDataService}.
     * @return The {@link VehicleDataService}.
     *
     * @HTTP 404 Route not found.
     */
    @Path("/{rid}/datapoint/")
    @RolesAllowed({PermissionType.Constants.USER})
    public VehicleDataService getVehicleDataService(@PathParam("rid") Long id) {
        Route found = routeDao.byId(id);

        if(found == null)
            throw new NotFoundException("Route not found with given id");

        vehicleDataService.setRoute(found);

        return vehicleDataService;
    }
}
