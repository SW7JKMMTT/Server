package rocks.stalin.sw708e16.server.persistence.given;

import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.vehicledata.VehicleData;
import rocks.stalin.sw708e16.server.core.vehicledata.VehicleDataPoint;
import rocks.stalin.sw708e16.server.persistence.VehicleDataDao;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class GivenVehicleData {
    private Route route;
    private Date timestamp;
    private Set<VehicleDataPoint> vehicleDataPoints = new HashSet<>();

    public GivenVehicleData() {
    }

    public GivenVehicleData withRoute(Route route) {
        this.route = route;
        return this;
    }

    public GivenVehicleData withTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public GivenVehicleData withDataPoint(VehicleDataPoint vehicleDataPoint) {
        this.vehicleDataPoints.add(vehicleDataPoint);
        return this;
    }

    public VehicleData in(VehicleDataDao vehicleDataDao) {
        VehicleData vehicleData = new VehicleData(route, timestamp, vehicleDataPoints);
        vehicleDataPoints.forEach(vehicleDataPoint -> vehicleDataPoint.setVehicleData(vehicleData));
        vehicleDataDao.add(vehicleData);
        return vehicleData;
    }
}
