package rocks.stalin.sw708e16.server.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import rocks.stalin.sw708e16.server.core.spatial.Route;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "Vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

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

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private VehicleIcon vehicleIcon;

    protected Vehicle() {
    }

    public Vehicle(String make, String model, int vintage, Vin vin) {
        this.make = make;
        this.model = model;
        this.vintage = vintage;
        this.vin = vin;
    }

    public long getId() {
        return id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getVintage() {
        return vintage;
    }

    public void setVintage(int vintage) {
        this.vintage = vintage;
    }

    public Vin getVin() {
        return vin;
    }

    public void setVin(Vin vin) {
        this.vin = vin;
    }

    public VehicleIcon getVehicleIcon() {
        return vehicleIcon;
    }

    @JsonIgnore
    public void setVehicleIcon(VehicleIcon vehicleIcon) {
        this.vehicleIcon = vehicleIcon;
    }

    @JsonIgnore
    public Collection<Route> getRoutes() {
        return routes;
    }

    public void addRoute(Route route) {
        this.routes.add(route);
    }

    @JsonGetter("isActive")
    public boolean isActive() {
        return this.routes.stream().anyMatch(route -> route.getRouteState().equals(RouteState.ACTIVE));
    }
}
