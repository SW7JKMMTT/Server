package rocks.stalin.sw708e16.server.core.vehicledata.concretedatapoints;

import com.fasterxml.jackson.annotation.JsonCreator;
import rocks.stalin.sw708e16.server.core.vehicledata.FloatVehicleDataPoint;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "2")
public class FuelLevelDataPoint extends FloatVehicleDataPoint {
    public FuelLevelDataPoint() {
    }

    @JsonCreator
    public FuelLevelDataPoint(Double value) {
        this.setValue(value);
    }
}
