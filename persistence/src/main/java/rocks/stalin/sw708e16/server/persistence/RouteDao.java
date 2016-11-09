package rocks.stalin.sw708e16.server.persistence;

import org.bson.types.ObjectId;
import rocks.stalin.sw708e16.server.core.spatial.Route;

import java.util.Collection;

public interface RouteDao extends BaseDao<Route> {
    // TODO: Spatial searches.

    Collection<Route> getAll();

    Route byId(ObjectId id);
}
