package rocks.stalin.sw708e16.server.core.vehicledata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SecondaryTable;

@Entity
@SecondaryTable(name = "FloatVehicleDataPoint")
public abstract class FloatVehicleDataPoint extends VehicleDataPoint {
    @Column(table = "FloatVehicleDataPoint")
    private Double value;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
