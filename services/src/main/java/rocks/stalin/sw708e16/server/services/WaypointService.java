package rocks.stalin.sw708e16.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;
import rocks.stalin.sw708e16.server.persistence.WaypointDao;
import rocks.stalin.sw708e16.server.services.builders.WaypointBuilder;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import java.util.Collection;
import java.util.Date;

@Transactional
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WaypointService {
    private Route route;

    @Autowired
    private WaypointDao waypointDao;

    Route getRoute() {
        return route;
    }

    void setRoute(Route route) {
        this.route = route;
    }

    /**
     * Gets all Waypoints for a given route, or a specified number of waypoints.
     *
     * @param maxCount The maximum number of waypoints to return, ordered by timestamp descending (newest first).
     * @param since The earliest time (epoch millis) to include waypoints from (exclusive).
     * @return All waypoints in the route.
     */
    @GET
    @Path("/")
    @Produces("application/json")
    @RolesAllowed({PermissionType.Constants.USER})
    public Collection<Waypoint> getWaypoints(
        @DefaultValue("0") @QueryParam("count") int maxCount,
        @DefaultValue("0") @QueryParam("byRoute_after") long since)
    {
        if (since != 0) {
            Date date = new Date(since);

            if (maxCount != 0)
                return waypointDao.byRoute_afterWithMaximum(route, date, maxCount);

            return waypointDao.byRoute_after(route, date);
        }

        if (maxCount == 0)
            return route.getWaypoints();

        return waypointDao.byRoute(route, maxCount);
    }

    /**
     * Add a waypoint to a route.
     *
     * @param waypointBuilder the waypoint to add.
     * @return The waypoint which was just added.
     */
    @POST
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    @RolesAllowed({PermissionType.Constants.USER})
    public Waypoint addWaypoint(WaypointBuilder waypointBuilder) {
        if (waypointBuilder.getLatitude() == null ||
                waypointBuilder.getLongitude() == null ||
                waypointBuilder.getTimestamp() == null)
        {
            throw new IllegalArgumentException("Invalid waypointbuilder");
        }

        Waypoint waypoint = waypointBuilder.build(route);
        route.addWaypoint(waypoint);
        waypointDao.add(waypoint);

        return waypoint;
    }
}
