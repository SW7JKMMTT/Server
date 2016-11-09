package rocks.stalin.sw708e16.server.core.spatial;

import org.bson.types.ObjectId;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.IntegerBridge;

import javax.persistence.*;
import java.util.Date;

@Entity
@Spatial
@Indexed
@Table(name = "Waypoint")
public class Waypoint {
    @Id
    @FieldBridge(impl = IntegerBridge.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    ObjectId id;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Latitude
    double latitude;

    @Longitude
    double longitude;

    @ManyToOne(optional = false)
    private Route route;

    public ObjectId getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
