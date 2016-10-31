package rocks.stalin.sw708e16.server.core;

import org.bson.types.ObjectId;
import rocks.stalin.sw708e16.server.core.spatial.Path;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Driver")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ObjectId id;

    @OneToOne(optional = false)
    private User user;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private List<Path> paths = new ArrayList<>();

    public ObjectId getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addPath(Path path) {
        paths.add(path);
    }
}
