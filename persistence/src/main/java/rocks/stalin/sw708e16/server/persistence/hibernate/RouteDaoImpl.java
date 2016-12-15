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
    public Route byId_ForWaypointService(long id) {
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
    public Route byId_ForVehicleDataService(long id) {
        TypedQuery<Route> query = em.createQuery(
            "SELECT r " +
                "FROM Route r " +
                "LEFT JOIN FETCH r.vehicleData as vd " +
                "LEFT JOIN FETCH vd.vehicleDataPoints " +
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
        if (waypoints.isEmpty())
            return new ArrayList<Route>();

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
        if (waypoints.isEmpty())
            return new ArrayList<Route>();

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
        if (waypoints.isEmpty())
            return new ArrayList<Route>();

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
        if (waypoints.isEmpty())
            return new ArrayList<Route>();

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
}
