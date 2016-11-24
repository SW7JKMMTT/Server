package rocks.stalin.sw708e16.server.core.spatial;

import org.bson.types.ObjectId;
import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.RouteState;
import rocks.stalin.sw708e16.server.core.Vehicle;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ObjectId id;

    @OneToMany(mappedBy = "route", fetch = FetchType.EAGER)
    @OrderBy("timestamp")
    private List<Waypoint> points = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Driver driver;

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

    public ObjectId getId() {
        return id;
    }

    public void addWaypoint(Waypoint point) {
        points.add(point);
    }

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

    /**
     * Gets the latest Waypoint in the Route.
     * @return The latest Waypoint or null if non is present.
     */
    public List<Waypoint> getLastWaypoints(int count) {
        List<Waypoint> waypoints = this.getWaypoints()
                .stream()
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()))
                .limit(count)
                .collect(Collectors.toList());

        return waypoints;
    }
}
