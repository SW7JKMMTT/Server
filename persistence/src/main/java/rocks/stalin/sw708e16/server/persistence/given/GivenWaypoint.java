package rocks.stalin.sw708e16.server.persistence.given;

import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;
import rocks.stalin.sw708e16.server.persistence.WaypointDao;

import java.util.Date;

public class GivenWaypoint {
    private Date timestamp;
    private double latitude;
    private double longitude;
    private Route route;

    public GivenWaypoint withTimeStamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public GivenWaypoint withLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public GivenWaypoint withLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public GivenWaypoint withRoute(Route route) {
        this.route = route;
        return this;
    }

    public Waypoint in(WaypointDao waypointDao) {
        Waypoint waypoint = new Waypoint(timestamp, latitude, longitude, route);
        waypointDao.add(waypoint);
        route.addWaypoint(waypoint);
        return waypoint;
    }

}
