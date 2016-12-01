package rocks.stalin.sw708e16.server.persistence;

import rocks.stalin.sw708e16.server.core.spatial.Waypoint;

import java.util.Collection;
import java.util.List;

public interface WaypointDao extends BaseDao<Waypoint> {
    List<Waypoint> withinRadius(Coordinate coordinate, double kilometers);
}
