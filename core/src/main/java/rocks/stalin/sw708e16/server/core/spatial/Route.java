package rocks.stalin.sw708e16.server.core.spatial;

import org.bson.types.ObjectId;
import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.Vehicle;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ObjectId id;

    @OneToMany(mappedBy = "route", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("timestamp")
    private List<Waypoint> points = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Vehicle vehicle;

    protected Route() {
    }

    public Route(List<Waypoint> points, Driver driver, Vehicle vehicle) {
        this.points = points;
        this.driver = driver;
        this.vehicle = vehicle;
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
}
