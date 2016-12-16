package rocks.stalin.sw708e16.server.persistence;


import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.vehicledata.VehicleData;

import java.util.List;

public interface VehicleDataDao extends BaseDao<VehicleData> {
    List<VehicleData> byRoute(Route route);
}
