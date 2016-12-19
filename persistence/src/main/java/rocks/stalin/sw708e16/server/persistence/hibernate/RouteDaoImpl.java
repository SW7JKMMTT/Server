package rocks.stalin.sw708e16.server.persistence.hibernate;


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
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Repository
@Primary
public class RouteDaoImpl extends BaseDaoImpl<Route> implements RouteDao {
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
    public Route byId_ForDisplay(long id) {
        TypedQuery<Route> query = em.createQuery(
            "SELECT DISTINCT r " +
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
            "SELECT DISTINCT r " +
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

    private Set<Long> routeIdsWithinRadius(WaypointDao waypointDao, Coordinate coordinate, Double radius) {
        Collection<Waypoint> waypoints = waypointDao.withinRadius(coordinate, radius);
        return waypoints
            .stream()
            .map((points) -> points.getRoute().getId())
            .distinct()
            .collect(Collectors.toSet());
    }

    @Override
    public Collection<Route> withinRadius_ForDisplay(WaypointDao waypointDao, Coordinate coordinate, Double radius) {
        Set<Long> routes = routeIdsWithinRadius(waypointDao, coordinate, radius);
        if (routes.isEmpty())
            return new ArrayList<>();

        TypedQuery<Route> query = em.createQuery(
            "SELECT DISTINCT r " +
                "FROM Route r " +
                "JOIN FETCH r.points " +
                "JOIN FETCH r.vehicle v " +
                "JOIN FETCH r.driver d " +
                "WHERE r.id IN :routes",
            Route.class);

        query.setParameter("routes", routes);
        return query.getResultList();
    }

    @Override
    public Collection<Route> withinRadius_ForDisplay(
        WaypointDao waypointDao,
        Coordinate coordinate,
        Double radius,
        Driver driver)
    {
        Set<Long> routes = routeIdsWithinRadius(waypointDao, coordinate, radius);
        if (routes.isEmpty())
            return new ArrayList<>();

        TypedQuery<Route> query = em.createQuery(
            "SELECT DISTINCT r " +
                "FROM Route r " +
                "JOIN FETCH r.points " +
                "JOIN FETCH r.vehicle v " +
                "JOIN FETCH r.driver d " +
                "WHERE r.id IN :routes " +
                "AND d = :driver",
            Route.class);

        query.setParameter("routes", routes);
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
        Set<Long> routes = routeIdsWithinRadius(waypointDao, coordinate, radius);
        if (routes.isEmpty())
            return new ArrayList<>();

        TypedQuery<Route> query = em.createQuery(
            "SELECT DISTINCT r " +
                "FROM Route r " +
                "JOIN FETCH r.points " +
                "JOIN FETCH r.vehicle v " +
                "JOIN FETCH r.driver d " +
                "WHERE r.id IN :routes " +
                "AND r.routeState = :state",
            Route.class);

        query.setParameter("routes", routes);
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
        Set<Long> routes = routeIdsWithinRadius(waypointDao, coordinate, radius);
        if (routes.isEmpty())
            return new ArrayList<>();

        TypedQuery<Route> query = em.createQuery(
            "SELECT DISTINCT r " +
                "FROM Route r " +
                "JOIN FETCH r.points p " +
                "JOIN FETCH r.vehicle v " +
                "JOIN FETCH r.driver d " +
                "WHERE r.id IN :routes " +
                "AND d = :driver " +
                "AND r.routeState = :state",
            Route.class);

        query.setParameter("routes", routes);
        query.setParameter("driver", driver);
        query.setParameter("state", state);
        return query.getResultList();
    }
}
