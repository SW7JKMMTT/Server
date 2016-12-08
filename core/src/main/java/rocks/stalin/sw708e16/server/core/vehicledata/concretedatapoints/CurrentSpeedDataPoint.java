package rocks.stalin.sw708e16.server.core.vehicledata.concretedatapoints;

import rocks.stalin.sw708e16.server.core.vehicledata.FloatVehicleDataPoint;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Given in meters pr. second [m/s].
 */
@Entity
@DiscriminatorValue(value = "1")
public class CurrentSpeedDataPoint extends FloatVehicleDataPoint {
    public CurrentSpeedDataPoint() {
    }

    public CurrentSpeedDataPoint(Double value) {
        this.setValue(value);
    }
}
