package rocks.stalin.sw708e16.server.persistence;

import org.bson.types.ObjectId;
import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.RouteState;
import rocks.stalin.sw708e16.server.core.spatial.Route;

import java.util.Collection;

public interface RouteDao extends BaseDao<Route> {
    // TODO: Spatial searches.

    Collection<Route> getAll_ForDisplay();

    Route byId_ForDisplay(ObjectId id);

    Collection<Route> getByState_ForDisplay(RouteState routeState);

    Route byId(ObjectId id);

    Collection<Route> getByDriver_ForDisplay(Driver driver);

    Collection<Route> getByDriverAndState_ForDisplay(RouteState routeState, Driver driver);

    Collection<Route> withinRadius_ForDisplay(WaypointDao waypointDao, Coordinate coordinate, Double radius);

    Collection<Route> withinRadius_ForDisplay(WaypointDao waypointDao, Coordinate coordinate, Double radius, Driver driver);

    Collection<Route> withinRadius_ForDisplay(WaypointDao waypointDao, Coordinate coordinate, Double radius, RouteState routeState);

    Collection<Route> withinRadius_ForDisplay(WaypointDao waypointDao, Coordinate coordinate, Double radius, Driver driver, RouteState routeState);
}
