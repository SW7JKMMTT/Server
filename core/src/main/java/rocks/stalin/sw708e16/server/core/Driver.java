package rocks.stalin.sw708e16.server.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import org.bson.types.ObjectId;
import rocks.stalin.sw708e16.server.core.spatial.Route;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "Driver")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ObjectId id;

    @OneToOne(optional = false)
    private User user;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private Collection<Route> routes = new ArrayList<>();

    protected Driver() {
    }

    public Driver(User user) {
        this.user = user;
    }

    public ObjectId getId() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Driver driver = (Driver) o;

        if (!id.equals(driver.id)) return false;
        return user.equals(driver.user);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }
}
