package rocks.stalin.sw708e16.server.core;

import org.bson.types.ObjectId;
import rocks.stalin.sw708e16.server.core.spatial.Route;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ObjectId id;

    @Column(unique = false, nullable = false)
    private String make;

    @Column(unique = false, nullable = false)
    private String model;

    @Column(unique = false, nullable = false)
    private int vintage;

    @Column(unique = false, nullable = false)
    private Vin vin;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private Collection<Route> routes = new ArrayList<>();

    protected Vehicle() {
    }

    public Vehicle(String make, String model, int vintage, Vin vin) {
        this.make = make;
        this.model = model;
        this.vintage = vintage;
        this.vin = vin;
    }

    public ObjectId getId() {
        return id;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getVintage() {
        return vintage;
    }

    public Vin getVin() {
        return vin;
    }

    public Collection<Route> getRoutes() {
        return routes;
    }

    public void addPath(Route route) {
        this.routes.add(route);
    }
}
