package rocks.stalin.sw708e16.server.services;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Collection;

@Transactional
@Component
public class WaypointService {
    private Route route;

    void setRoute(Route route) {
        this.route = route;
    }

    @GET
    @Path("/")
    @Produces("application/json")
    public Collection<Waypoint> getAllWaypointsInRoute() {
        return route.getWaypoints();
    }

    @GET
    @Path("/last")
    @Produces("application/json")
    public Waypoint getLatestWaypoint() {
        if(route.getWaypoints().size() == 0)
            throw new NotFoundException();

        return route.getWaypoints().get(route.getWaypoints().size());
    }
}
