package rocks.stalin.sw708e16.server.core.spatial;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.IntegerBridge;
import rocks.stalin.sw708e16.server.persistence.ObjectIdBridge;

import javax.persistence.*;
import java.util.Date;

@Entity
@Spatial
@Indexed
@Table(name = "Waypoint")
public class Waypoint {
    @Id
    @FieldBridge(impl = ObjectIdBridge.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    ObjectId id;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Latitude
    private double latitude;

    @Longitude
    private double longitude;

    @ManyToOne(optional = false)
    private Route route;

    protected Waypoint() {
    }

    public Waypoint(Date timestamp, double latitude, double longitude, Route route) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.route = route;
    }

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

    @JsonIgnore
    public Route getRoute() {
        return route;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Waypoint waypoint = (Waypoint) obj;

        if (this.getId() != null && waypoint.getId() != null) {
            return this.getId().equals(waypoint.getId());
        }

        if (Double.compare(waypoint.latitude, latitude) != 0) return false;
        if (Double.compare(waypoint.longitude, longitude) != 0) return false;
        if (id != null ? !id.equals(waypoint.id) : waypoint.id != null) return false;
        if (!timestamp.equals(waypoint.timestamp)) return false;
        return route.equals(waypoint.route);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        
        if (id != null)
            return id.hashCode();

        result = timestamp.hashCode();
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + route.hashCode();
        return result;
    }
}
