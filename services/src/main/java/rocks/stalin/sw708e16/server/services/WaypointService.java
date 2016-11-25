package rocks.stalin.sw708e16.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;
import rocks.stalin.sw708e16.server.persistence.WaypointDao;
import rocks.stalin.sw708e16.server.services.builders.WaypointBuilder;

import javax.ws.rs.*;
import java.util.Collection;
import java.util.Optional;

@Transactional
@Component
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
     * Gets all Waypoints for a given route.
     *
     * @return All waypoints in the route.
     */
    @GET
    @Path("/")
    @Produces("application/json")
    public Collection<Waypoint> getWaypoints(@DefaultValue("0") @QueryParam("count") int maxCount) {
        if (maxCount == 0)
            return route.getWaypoints();

        return route.getLastWaypoints(maxCount);
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
