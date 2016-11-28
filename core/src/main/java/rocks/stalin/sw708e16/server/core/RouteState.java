package rocks.stalin.sw708e16.server.core;

import rocks.stalin.sw708e16.server.core.spatial.Route;

/**
 * The states which a {@link Route} can take.
 */
public enum RouteState {
    /**
     * Indicates that a route has been created, but no waypoints are active on it yet.
     */
    CREATED,
    /**
     * Indicates that a route has been created, and at least one waypoint have been added, but is not completed.
     */
    ACTIVE,
    /**
     * Indicates that a route is completed.
     */
    COMPLETE
}
