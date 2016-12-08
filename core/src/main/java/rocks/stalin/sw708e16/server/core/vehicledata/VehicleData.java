package rocks.stalin.sw708e16.server.core.vehicledata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import rocks.stalin.sw708e16.server.core.spatial.Route;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "VehicleData")
public class VehicleData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToMany(mappedBy = "vehicleData", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<VehicleDataPoint> vehicleDataPoints = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Route route;

    @Column(nullable = false)
    private Date timestamp;

    public VehicleData() {
    }

    public VehicleData(Route route, Date timestamp) {
        this.route = route;
        this.timestamp = timestamp;
    }

    public VehicleData(Route route, Date timestamp, Set<VehicleDataPoint> vehicleDataPoints) {
        this.vehicleDataPoints = vehicleDataPoints;
        this.route = route;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public Collection<VehicleDataPoint> getVehicleDataPoints() {
        return Collections.unmodifiableSet(vehicleDataPoints);
    }

    public void addVehicleDataPoint(VehicleDataPoint vehicleDataPoint) {
        vehicleDataPoints.add(vehicleDataPoint);
    }

    @JsonIgnore
    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        VehicleData that = (VehicleData) obj;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
