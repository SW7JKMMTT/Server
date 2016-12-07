package rocks.stalin.sw708e16.server.persistence.hibernate;


import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Transactional
@Repository
@Primary
public class RouteDaoImpl extends BaseDaoImpl<Route> implements RouteDao {
    private static Logger logger = LoggerFactory.getLogger(RouteDaoImpl.class);

    @Override
    public Collection<Route> getAll_ForDisplay() {
        TypedQuery<Route> query = em.createQuery(
            "SELECT DISTINCT r " +
                "FROM Route r " +
                "JOIN FETCH r.driver " +
                "JOIN FETCH r.vehicle",
            Route.class);
        return query.getResultList();
    }

    @Override
    public Route byId(long id) {
        TypedQuery<Route> query = em.createQuery(
            "SELECT r " +
                "FROM Route r " +
                "WHERE r.id = :id",
                Route.class);
        query.setParameter("id", id);
        return getFirst(query);
    }

    @Override
    public Route byId_ForWaypoint(long id) {
        TypedQuery<Route> query = em.createQuery(
            "SELECT r " +
                "FROM Route r " +
                "LEFT JOIN FETCH r.points " +
                "WHERE r.id = :id",
            Route.class);
        query.setParameter("id", id);
        return getFirst(query);
    }

    @Override
    public Route byId_ForDisplay(long id) {
        TypedQuery<Route> query = em.createQuery(
            "SELECT r " +
                "FROM Route r " +
                "JOIN FETCH r.driver " +
                "JOIN FETCH r.vehicle " +
                "WHERE r.id = :id",
            Route.class);
        query.setParameter("id", id);
        return getFirst(query);
    }

    @Override
    public Collection<Route> getByState_ForDisplay(RouteState routeState) {
        TypedQuery<Route> query = em.createQuery(
            "SELECT r " +
                "FROM Route r " +
                "JOIN FETCH r.driver " +
                "JOIN FETCH r.vehicle " +
                "WHERE r.routeState = :state",
                Route.class);
        query.setParameter("state", routeState);
        return query.getResultList();
    }

    @Override
    public Collection<Route> getByDriver_ForDisplay(Driver driver) {
        TypedQuery<Route> query = em.createQuery(
            "SELECT DISTINCT r " +
                "FROM Route r " +
                "JOIN FETCH r.driver d " +
                "JOIN FETCH r.vehicle " +
                "WHERE d = :driver",
            Route.class);
        query.setParameter("driver", driver);
        return query.getResultList();
    }

    @Override
    public Collection<Route> getByDriverAndState_ForDisplay(RouteState routeState, Driver driver) {
        TypedQuery<Route> query = em.createQuery(
            "SELECT DISTINCT r " +
                "FROM Route r " +
                "JOIN FETCH r.driver d " +
                "JOIN FETCH r.vehicle " +
                "WHERE d = :driver " +
                    "AND r.routeState = :state",
            Route.class);
        query.setParameter("driver", driver);
        query.setParameter("state", routeState);
        return query.getResultList();
    }

    @Override
    public Collection<Route> withinRadius_ForDisplay(WaypointDao waypointDao, Coordinate coordinate, Double radius) {
        Collection<Waypoint> waypoints = waypointDao.withinRadius(coordinate, radius);

        TypedQuery<Route> query = em.createQuery(
            "SELECT DISTINCT r " +
                "FROM Route r " +
                "JOIN FETCH r.points p " +
                "JOIN FETCH r.vehicle v " +
                "JOIN FETCH r.driver d " +
                "WHERE p IN :wayp",
            Route.class);

        query.setParameter("wayp", waypoints);
        return query.getResultList();
    }

    @Override
    public Collection<Route> withinRadius_ForDisplay(
        WaypointDao waypointDao,
        Coordinate coordinate,
        Double radius,
        Driver driver)
    {
        Collection<Waypoint> waypoints = waypointDao.withinRadius(coordinate, radius);

        TypedQuery<Route> query = em.createQuery(
            "SELECT DISTINCT r " +
                "FROM Route r " +
                "JOIN FETCH r.points p " +
                "JOIN FETCH r.vehicle v " +
                "JOIN FETCH r.driver d " +
                "WHERE p IN :wayp " +
                "AND d = :driver",
            Route.class);

        query.setParameter("wayp", waypoints);
        query.setParameter("driver", driver);
        return query.getResultList();
    }

    @Override
    public Collection<Route> withinRadius_ForDisplay(
        WaypointDao waypointDao,
        Coordinate coordinate,
        Double radius,
        RouteState state)
    {
        Collection<Waypoint> waypoints = waypointDao.withinRadius(coordinate, radius);

        TypedQuery<Route> query = em.createQuery(
            "SELECT DISTINCT r " +
                "FROM Route r " +
                "JOIN FETCH r.points p " +
                "JOIN FETCH r.vehicle v " +
                "JOIN FETCH r.driver d " +
                "WHERE p IN :wayp " +
                "AND r.routeState = :state",
            Route.class);

        query.setParameter("wayp", waypoints);
        query.setParameter("state", state);
        return query.getResultList();
    }

    @Override
    public Collection<Route> withinRadius_ForDisplay(
        WaypointDao waypointDao,
        Coordinate coordinate,
        Double radius,
        Driver driver,
        RouteState state)
    {
        Collection<Waypoint> waypoints = waypointDao.withinRadius(coordinate, radius);

        TypedQuery<Route> query = em.createQuery(
            "SELECT DISTINCT r " +
                "FROM Route r " +
                "JOIN FETCH r.points p " +
                "JOIN FETCH r.vehicle v " +
                "JOIN FETCH r.driver d " +
                "WHERE p IN :wayp " +
                "AND d = :driver " +
                "AND r.routeState = :state",
            Route.class);

        query.setParameter("wayp", waypoints);
        query.setParameter("driver", driver);
        query.setParameter("state", state);
        return query.getResultList();
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
