package rocks.stalin.sw708e16.server.persistence.hibernate;


import org.bson.types.ObjectId;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.core.RouteState;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;
import rocks.stalin.sw708e16.server.persistence.Coordinate;
import rocks.stalin.sw708e16.server.persistence.RouteDao;
import rocks.stalin.sw708e16.server.persistence.WaypointDao;
import rocks.stalin.sw708e16.server.persistence.hibernate.magic.HibernateMagic;

import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Transactional
@Repository
@Primary
public class RouteDaoImpl extends BaseDaoImpl<Route> implements RouteDao {
    @Override
    public Collection<Route> getAll_ForDisplay() {
        TypedQuery<Route> query = em.createQuery("SELECT r FROM Route r", Route.class);
        return initialize_ForDisplay(query.getResultList());
    }

    @Override
    public Route byId(ObjectId id) {
        TypedQuery<Route> query = em.createQuery(
                "SELECT r " +
                        "FROM Route r " +
                        "WHERE r.id = :id",
                Route.class);
        query.setParameter("id", id);
        return getFirst(query);
    }

    @Override
    public Route byId_ForDisplay(ObjectId id) {
        TypedQuery<Route> query = em.createQuery(
            "SELECT r " +
                "FROM Route r " +
                "WHERE r.id = :id",
            Route.class);
        query.setParameter("id", id);
        return initialize_ForDisplay(getFirst(query));
    }

    @Override
    public Collection<Route> getByState_ForDisplay(RouteState routeState) {
        TypedQuery<Route> query = em.createQuery(
            "SELECT r " +
                    "FROM Route r " +
                    "WHERE r.routeState = :state",
                Route.class);
        query.setParameter("state", routeState);
        return initialize_ForDisplay(query.getResultList());
    }

    @Override
    public Collection<Route> getByDriver_ForDisplay(Driver driver) {
        return initialize_ForDisplay(driver.getRoutes());
    }

    @Override
    public Collection<Route> getByDriverAndState_ForDisplay(RouteState routeState, Driver driver) {
        TypedQuery<Route> query = em.createQuery(
            "SELECT r " +
                "FROM Route r " +
                "WHERE r.driver = :driver " +
                "AND r.routeState = :state",
            Route.class);
        query.setParameter("driver", driver);
        query.setParameter("state", routeState);
        return initialize_ForDisplay(query.getResultList());
    }

    @Override
    public Collection<Route> withinRadius_ForDisplay(WaypointDao waypointDao, Coordinate coordinate, Double radius) {
        // I'm doing this in the most naive version ever, can get improved later if needed.
        Collection<Waypoint> waypoints = waypointDao.withinRadius(coordinate, radius);

        LinkedHashMap<Route, Boolean> routesMap = new LinkedHashMap<>();
        for (Waypoint waypoint : waypoints) {
            Route route = waypoint.getRoute();
            if (!routesMap.containsKey(route)) {
                Hibernate.initialize(route.getVehicle());
                Hibernate.initialize(route.getDriver());
                routesMap.put(route, true);
            }
        }

        return routesMap.keySet();
    }

    @Override
    public Collection<Route> withinRadius_ForDisplay(WaypointDao waypointDao, Coordinate coordinate, Double radius, Driver driver) {
        Collection<Route> routes = withinRadius_ForDisplay(waypointDao, coordinate, radius);
        return routes
            .stream()
            .filter(route -> route.getDriver().equals(driver))
            .collect(Collectors.toList());
    }

    @Override
    public Collection<Route> withinRadius_ForDisplay(WaypointDao waypointDao, Coordinate coordinate, Double radius, RouteState routeState) {
        Collection<Route> routes = withinRadius_ForDisplay(waypointDao, coordinate, radius);
        return routes
            .stream()
            .filter(route -> route.getRouteState().equals(routeState))
            .collect(Collectors.toList());
    }

    @Override
    public Collection<Route> withinRadius_ForDisplay(WaypointDao waypointDao, Coordinate coordinate, Double radius, Driver driver, RouteState routeState) {
        Collection<Route> routes = withinRadius_ForDisplay(waypointDao, coordinate, radius);
        return routes
            .stream()
            .filter(route -> route.getDriver().equals(driver))
            .filter(route -> route.getRouteState().equals(routeState))
            .collect(Collectors.toList());
    }

    /**
     * Initializes the lazy fields in each element in the {@link Route routes}.
     *
     * @param routes {@link Route routes} to initialize.
     * @return The {@link Route routes} with initialized fields.
     */
    private Collection<Route> initialize_ForDisplay(Collection<Route> routes) {
        for (Route route : routes) {
            initialize_ForDisplay(route);
        }

        return routes;
    }

    /**
     * Initializes the lazy fields in the {@link Route}.
     * @param route {@link Route} to initialize.
     * @return The {@link Route} with initialized fields.
     */
    private Route initialize_ForDisplay(Route route) {
        if (route == null)
            return route;

        Hibernate.initialize(route.getDriver());
        Hibernate.initialize(route.getVehicle());
        return route;
    }
}
