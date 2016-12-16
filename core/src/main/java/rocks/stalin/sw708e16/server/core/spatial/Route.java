package rocks.stalin.sw708e16.server.core.spatial;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.RouteState;
import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.vehicledata.VehicleData;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("timestamp")
    private SortedSet<Waypoint> points;

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<VehicleData> vehicleData = new HashSet<>();

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Driver driver;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RouteState routeState;

    protected Route() {
        routeState = RouteState.CREATED;
        points = Collections.synchronizedSortedSet(new TreeSet<Waypoint>());
    }

    public Route(Driver driver, Vehicle vehicle) {
        this();
        this.driver = driver;
        this.vehicle = vehicle;
        this.routeState = this.points.isEmpty() ? RouteState.CREATED : RouteState.ACTIVE;
    }

    public long getId() {
        return id;
    }

    public void addWaypoint(Waypoint point) {
        points.add(point);
        this.routeState = RouteState.ACTIVE;
    }

    @JsonIgnore
    public SortedSet<Waypoint> getWaypoints() {
        return points;
    }

    public void addVehicleData(VehicleData vehicleData) {
        this.vehicleData.add(vehicleData);
    }

    @JsonIgnore
    public Set<VehicleData> getVehicleData() {
        return Collections.unmodifiableSet(vehicleData);
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public RouteState getRouteState() {
        return routeState;
    }

    public void setRouteState(RouteState routeState) {
        this.routeState = routeState;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Route route = (Route) obj;

        return id == route.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
