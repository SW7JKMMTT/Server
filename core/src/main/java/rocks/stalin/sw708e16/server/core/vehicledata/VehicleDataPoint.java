package rocks.stalin.sw708e16.server.core.vehicledata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.webcohesion.enunciate.metadata.json.JsonSeeAlso;
import rocks.stalin.sw708e16.server.core.vehicledata.concretedatapoints.CurrentSpeedDataPoint;
import rocks.stalin.sw708e16.server.core.vehicledata.concretedatapoints.FuelLevelDataPoint;

import javax.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DataType", discriminatorType = DiscriminatorType.INTEGER)
@JsonSeeAlso( {FuelLevelDataPoint.class, CurrentSpeedDataPoint.class} )
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes( {
    @JsonSubTypes.Type(value = CurrentSpeedDataPoint.class, name = "currentspeed"),
    @JsonSubTypes.Type(value = FuelLevelDataPoint.class, name = "fuellevel")
} )
public abstract class VehicleDataPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private VehicleData vehicleData;

    public VehicleDataPoint() {
    }

    @JsonIgnore
    public VehicleData getVehicleData() {
        return vehicleData;
    }

    public void setVehicleData(VehicleData vehicleData) {
        this.vehicleData = vehicleData;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        VehicleDataPoint that = (VehicleDataPoint) obj;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
