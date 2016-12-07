package rocks.stalin.sw708e16.server.core.spatial;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.RouteState;
import rocks.stalin.sw708e16.server.core.Vehicle;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("timestamp")
    private List<Waypoint> points = new ArrayList<>();

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
    }

    public Route(List<Waypoint> points, Driver driver, Vehicle vehicle) {
        this.points = points;
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
    public List<Waypoint> getWaypoints() {
        return points;
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

    //TODO: Only supports persisted objects
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
