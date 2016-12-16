package rocks.stalin.sw708e16.server.persistence;

import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;

import java.util.Date;
import java.util.List;

public interface WaypointDao extends BaseDao<Waypoint> {
    List<Waypoint> withinRadius(Coordinate coordinate, double kilometers);

    List<Waypoint> byRoute(Route route);

    List<Waypoint> byRoute(Route route, int count);

    List<Waypoint> byRoute_after(Route route, Date timestamp);

    List<Waypoint> byRoute_afterWithMaximum(Route route, Date timestamp, int count);
}
