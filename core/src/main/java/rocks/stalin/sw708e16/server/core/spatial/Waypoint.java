package rocks.stalin.sw708e16.server.core.spatial;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import org.hibernate.search.annotations.Spatial;

import javax.persistence.*;
import java.util.Date;

@Entity
@Spatial
@Indexed
@Table(name = "Waypoint")
public class Waypoint {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

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

    public long getId() {
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

        return id == waypoint.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
