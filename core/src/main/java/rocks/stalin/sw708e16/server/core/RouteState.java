package rocks.stalin.sw708e16.server.core;

import rocks.stalin.sw708e16.server.core.spatial.Route;

/**
 * The states which a {@link Route} can take.
 */
public enum RouteState {
    /**
     * Indicates that a route has been created, but no waypoints are active on it yet.
     */
    CREATED(Constants.CREATED),
    /**
     * Indicates that a route has been created, and at least one waypoint have been added, but is not completed.
     */
    ACTIVE(Constants.ACTIVE),
    /**
     * Indicates that a route is completed.
     */
    COMPLETE(Constants.COMPLETE);

    private final String routeStateStr;

    RouteState(String routeStateStr) {
        this.routeStateStr = routeStateStr;
    }

    public static RouteState fromString(String str) {
        return valueOf(str);
    }

    public static class Constants {
        public static final String CREATED = "Created";
        public static final String ACTIVE = "Active";
        public static final String COMPLETE = "Complete";
    }
}
