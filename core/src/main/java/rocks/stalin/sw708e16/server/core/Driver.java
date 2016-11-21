package rocks.stalin.sw708e16.server.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import rocks.stalin.sw708e16.server.core.spatial.Route;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

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

    public void addPath(Route route) {
        this.routes.add(route);
    }

    public void addPaths(Collection<Route> routes) {
        this.routes.addAll(routes);
    }


}
