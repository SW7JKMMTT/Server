package rocks.stalin.sw708e16.server.services.builders;

import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;

import java.util.Date;

public class WaypointBuilder {
    private Double latitude;
    private Double longitude;
    private Route route;
    private Date timestamp;

    public WaypointBuilder() {
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Construct the Waypoint if and only if all arguments are given.
     *
     * @return The Waypoint built.
     */
    public Waypoint build() {
        if (timestamp == null || latitude == null || longitude == null || route == null) {
            throw new IllegalArgumentException("All arguments must be given to the waypointbuilder");
        }

        return new Waypoint(timestamp, latitude, longitude, route);
    }
}
