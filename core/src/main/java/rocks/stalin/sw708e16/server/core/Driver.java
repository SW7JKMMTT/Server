package rocks.stalin.sw708e16.server.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import rocks.stalin.sw708e16.server.core.spatial.Route;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "Driver")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private Collection<Route> routes = new ArrayList<>();

    protected Driver() {
    }

    public Driver(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void addRoute(Route route) {
        this.routes.add(route);
    }

    @JsonIgnore
    public Collection<Route> getRoutes() {
        return Collections.unmodifiableCollection(routes);
    }

    public void addRoutes(Collection<Route> routes) {
        this.routes.addAll(routes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Driver driver = (Driver) obj;

        return id == driver.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
