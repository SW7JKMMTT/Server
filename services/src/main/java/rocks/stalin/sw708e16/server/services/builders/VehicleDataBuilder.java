package rocks.stalin.sw708e16.server.services.builders;

import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.vehicledata.VehicleData;
import rocks.stalin.sw708e16.server.core.vehicledata.VehicleDataPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class VehicleDataBuilder {
    private Date timestamp;
    private Collection<VehicleDataPoint> vehicleDataPoints = new ArrayList<>();

    public VehicleDataBuilder() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Collection<VehicleDataPoint> getVehicleDataPoints() {
        return vehicleDataPoints;
    }

    public void addVehicleDataPoint(VehicleDataPoint vehicleDataPoint) {
        this.vehicleDataPoints.add(vehicleDataPoint);
    }

    public VehicleData build(Route route) {
        if (route == null || timestamp == null || vehicleDataPoints.isEmpty())
            throw new IllegalArgumentException("Missing timestamp or data points");

        VehicleData vehicleData = new VehicleData(route, timestamp);
        for (VehicleDataPoint point : vehicleDataPoints) {
            vehicleData.addVehicleDataPoint(point);
            point.setVehicleData(vehicleData);
        }

        return vehicleData;
    }
}
