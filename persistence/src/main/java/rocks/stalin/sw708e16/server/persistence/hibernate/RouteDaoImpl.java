package rocks.stalin.sw708e16.server.persistence.hibernate;


import org.bson.types.ObjectId;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.RouteState;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.persistence.RouteDao;
import rocks.stalin.sw708e16.server.persistence.hibernate.magic.HibernateMagic;

import javax.persistence.TypedQuery;
import java.util.Collection;

@Transactional
@Repository
@Primary
public class RouteDaoImpl extends BaseDaoImpl<Route> implements RouteDao {
    @Override
    public Collection<Route> getAll_ForDisplay() {
        TypedQuery<Route> query = em.createQuery("SELECT p FROM Route p", Route.class);
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
            "SELECT p " +
                    "FROM Route p " +
                    "WHERE p.routeState = :state",
                Route.class);
        query.setParameter("state", routeState);
        return initialize_ForDisplay(query.getResultList());
    }

    /**
     * Uses {@link HibernateMagic} to initilize the lazy fields in each element in the {@link Route routes}.
     *
     * @param routes {@link Route routes} to initilize.
     * @return The {@link Route routes} with initilized fields.
     */
    private Collection<Route> initialize_ForDisplay(Collection<Route> routes) {
        for (Route route : routes) {
            initialize_ForDisplay(route);
        }

        return routes;
    }

    private Route initialize_ForDisplay(Route route) {
        if (route == null)
            return route;

        HibernateMagic.initialize(route, "driver");
        HibernateMagic.initialize(route, "vehicle");
        return route;
    }
}
